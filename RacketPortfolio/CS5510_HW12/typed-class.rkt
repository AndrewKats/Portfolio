#lang plai-typed

(require "class.rkt"
         "inherit.rkt")

(define-type ClassT
  [classT (name : symbol)
          (super-name : symbol)
          (fields : (listof FieldT))
          (methods : (listof MethodT))])

(define-type FieldT
  [fieldT (name : symbol)
          (type : Type)])

(define-type MethodT
  [methodT (name : symbol)
           (arg-type : Type)
           (result-type : Type)
           (body-expr : ExprI)])

(define-type Type
  [numT]
  [objT (class-name : symbol)]
  [nullT])

(module+ test
  (print-only-errors true))

;; ----------------------------------------

(define find-classT
  (make-find classT-name))

(define find-fieldT
  (make-find fieldT-name))

(define find-methodT
  (make-find methodT-name))

(define (type-error a msg)
  (error 'typecheck (string-append
                     "no type: "
                     (string-append
                      (to-string a)
                      (string-append " not "
                                     msg)))))

(define (get-all-field-types class-name t-classes)
  (if (equal? class-name 'object)
      empty        
      (type-case ClassT (find-classT class-name t-classes)
        [classT (name super-name fields methods)
                (append 
                 (get-all-field-types super-name t-classes)
                 (map fieldT-type fields))])))

;; ----------------------------------------

(define (make-find-in-tree find-in-list extract)
  (lambda (name t-class t-classes)
    (local [(define items (extract t-class))
            (define super-name 
              (classT-super-name t-class))]
      (if (equal? super-name 'object)
          (find-in-list name items)
          (try (find-in-list name items)
               (lambda ()
                 ((make-find-in-tree find-in-list extract)
                  name 
                  (find-classT super-name t-classes)
                  t-classes)))))))

(define find-field-in-tree
  (make-find-in-tree find-fieldT classT-fields))

(define find-method-in-tree
  (make-find-in-tree find-methodT classT-methods))

;; ----------------------------------------

(define (is-subclass? name1 name2 t-classes)
  (cond
    [(equal? name1 name2) true]
    [(equal? name1 'object) false]
    [else
     (type-case ClassT (find-classT name1 t-classes)
       [classT (name super-name fields methods)
               (is-subclass? super-name name2 t-classes)])]))

(define (is-subtype? t1 t2 t-classes)
  (type-case Type t1
    [objT (name1)
          (type-case Type t2 
            [objT (name2)
                  (is-subclass? name1 name2 t-classes)]
            [else false])]
    [else (equal? t1 t2)]))

(define (is-subtype-or-null? t1 t2 t-classes)
  (type-case Type t1
    [objT (name1)
          (type-case Type t2 
            [objT (name2)
                  (is-subclass? name1 name2 t-classes)]
            [else false])]
    [nullT () (type-case Type t2
                [numT () false]
                [else true])]
    [else (equal? t1 t2)]))

(module+ test
  (define a-t-class (classT 'a 'object empty empty))
  (define b-t-class (classT 'b 'a empty empty))

  (test (is-subclass? 'object 'object empty)
        true)
  (test (is-subclass? 'a 'b (list a-t-class b-t-class))
        false)
  (test (is-subclass? 'b 'a (list a-t-class b-t-class))
        true)

  (test (is-subtype? (numT) (numT) empty)
        true)
  (test (is-subtype? (numT) (objT 'object) empty)
        false)
  (test (is-subtype? (objT 'object) (numT) empty)
        false)
  (test (is-subtype? (objT 'a) (objT 'b) (list a-t-class b-t-class))
        false)
  (test (is-subtype? (objT 'b) (objT 'a) (list a-t-class b-t-class))
        true)
  (test (is-subtype-or-null? (nullT) (objT 'a) (list a-t-class b-t-class))
        true)
  (test (is-subtype-or-null? (objT 'a) (nullT) (list a-t-class b-t-class))
        false)
  )

;; ----------------------------------------

(define typecheck-expr : (ExprI (listof ClassT) Type Type boolean -> Type)
  (lambda (expr t-classes arg-type this-type isMain)
    (local [(define (recur expr)
              (typecheck-expr expr t-classes arg-type this-type isMain))
            (define (typecheck-nums l r)
              (type-case Type (recur l) 
                [numT ()
                      (type-case Type (recur r)
                        [numT () (numT)]
                        [else (type-error r "num")])]
                [else (type-error l "num")]))]
      (type-case ExprI expr
        [numI (n) (numT)]
        [plusI (l r) (typecheck-nums l r)] 
        [multI (l r) (typecheck-nums l r)]
        [argI () (cond                   
                   [isMain (error 'typecheck "arg in main exp")]
                   [else arg-type])]
        [thisI () (cond                   
                   [isMain (error 'typecheck "this in main exp")]
                   [else this-type])]
        [newI (class-name exprs)
              (local [(define arg-types (map recur exprs))
                      (define field-types
                        (get-all-field-types class-name t-classes))]
                (if (and (= (length arg-types) (length field-types))
                         (foldl (lambda (b r) (and r b))
                                true
                                (map2 (lambda (t1 t2) 
                                        (is-subtype-or-null? t1 t2 t-classes))
                                      arg-types
                                      field-types)))
                    (objT class-name)
                    (type-error expr "field type mismatch")))]
        [getI (obj-expr field-name)
              (type-case Type (recur obj-expr)
                [objT (class-name)
                      (local [(define t-class
                                (find-classT class-name t-classes))
                              (define field
                                (find-field-in-tree field-name
                                                    t-class
                                                    t-classes))]
                        (type-case FieldT field
                          [fieldT (name type) type]))]
                [else (type-error obj-expr "object")])]
        [sendI (obj-expr method-name arg-expr)
               (local [(define obj-type (recur obj-expr))
                       (define arg-type (recur arg-expr))]
                 (type-case Type obj-type
                   [objT (class-name)
                         (typecheck-send class-name method-name
                                         arg-expr arg-type
                                         t-classes)]
                   [else
                    (type-error obj-expr "object")]))]
        [superI (method-name arg-expr)
                (local [(define arg-type (recur arg-expr))
                        (define this-class
                          (find-classT (objT-class-name this-type)
                                       t-classes))]
                  (typecheck-send (classT-super-name this-class)
                                  method-name
                                  arg-expr arg-type
                                  t-classes))]
        [if0I (con thn els)
              (type-case Type (typecheck-expr con t-classes arg-type this-type isMain)
                [numT ()
                      (type-case Type (typecheck-expr thn t-classes arg-type this-type isMain)
                        [numT () (type-case Type (typecheck-expr els t-classes arg-type this-type isMain)
                                   [numT () (numT)]
                                   [else (type-error thn "num")])]
                        [objT (n) (type-case Type (typecheck-expr els t-classes arg-type this-type isMain)
                                    [objT (n) (if-type-helper (newI-class-name thn) (newI-class-name els) t-classes)]
                                    [else (type-error thn "object")])]
                        [nullT () (type-case Type (typecheck-expr els t-classes arg-type this-type isMain)
                                   [nullT () (nullT)]
                                   [else (type-error thn "num")])])]
                [else (type-error con "num")])]
        [instanceofI (obj name)
                     (type-case Type (recur obj)
                       [objT (class-name) (numT)]
                       [else (type-error obj "object")])]
        [castI (castname exp)
               (type-case Type (recur exp)
                       [objT (class-name) (cond
                                            [(is-subclass? class-name castname t-classes) (objT castname)]
                                            [(is-subclass? castname class-name t-classes) (objT castname)]
                                            [else (type-error exp "not sub or super type")])]
                       [else (type-error exp "object")])]
        [nullI () (nullT)]))))

(define (if-type-helper [thn : symbol] [els : symbol] [t-classes : (listof ClassT)]) : Type
  (cond
    [(is-subclass? thn els t-classes) (objT els)]
    [(is-subclass? els thn t-classes) (objT thn)]
    [else (find-common-parent thn els t-classes)]))

(define (find-common-parent [thn : symbol] [els : symbol] [t-classes : (listof ClassT)]) : Type
  (cond
    [(is-subclass? els thn t-classes) (objT thn)] ; found it   
    [else (find-common-parent (classT-super-name (find-classT thn t-classes)) els t-classes)]))

(define (typecheck-send [class-name : symbol]
                        [method-name : symbol]
                        [arg-expr : ExprI]
                        [arg-type : Type]
                        [t-classes : (listof ClassT)])
  (type-case MethodT (find-method-in-tree
                      method-name
                      (find-classT class-name t-classes)
                      t-classes)
    [methodT (name arg-type-m result-type body-expr)
             (if (or (and (nullT? arg-type) (not (numT? arg-type-m))) (is-subtype? arg-type arg-type-m t-classes)) ;changed for null cases
                 result-type
                 (type-error arg-expr (to-string arg-type-m)))]))

(define (typecheck-method [method : MethodT]
                          [this-type : Type]
                          [t-classes : (listof ClassT)]) : ()
  (type-case MethodT method
    [methodT (name arg-type result-type body-expr)
             (if (is-subtype? (typecheck-expr body-expr t-classes
                                              arg-type this-type #f)
                              result-type
                              t-classes)
                 (values)
                 (type-error body-expr (to-string result-type)))]))

(define (check-override [method : MethodT]
                        [this-class : ClassT]
                        [t-classes : (listof ClassT)])
  (local [(define super-name 
            (classT-super-name this-class))
          (define super-method
            (try
             ;; Look for method in superclass:
             (find-method-in-tree (methodT-name method)
                                  (find-classT super-name t-classes)
                                  t-classes)
             ;; no such method in superclass:
             (lambda () method)))]
    (if (and (equal? (methodT-arg-type method)
                     (methodT-arg-type super-method))
             (equal? (methodT-result-type method)
                     (methodT-result-type super-method)))
        (values)
        (error 'typecheck (string-append
                           "bad override of "
                           (to-string (methodT-name method)))))))

(define (typecheck-class [t-class : ClassT] [t-classes : (listof ClassT)])
  (type-case ClassT t-class
    [classT (name super-name fields methods)
            (map (lambda (m)
                   (begin
                     (typecheck-method m (objT name) t-classes)
                     (check-override m t-class t-classes)))
                 methods)]))

(define (typecheck [a : ExprI] [t-classes : (listof ClassT)]) : Type
  (begin
    (map (lambda (t-class)
           (typecheck-class t-class t-classes))
         t-classes)
    (typecheck-expr a t-classes (numT) (objT 'bad) #t)))

;; ----------------------------------------

(module+ test
  (define posn-t-class
    (classT 'posn 'object
            (list (fieldT 'x (numT)) (fieldT 'y (numT)))
            (list (methodT 'mdist (numT) (numT) 
                           (plusI (getI (thisI) 'x) (getI (thisI) 'y)))
                  (methodT 'addDist (objT 'posn) (numT)
                           (plusI (sendI (thisI) 'mdist (numI 0))
                                  (sendI (argI) 'mdist (numI 0)))))))

  (define posn3D-t-class 
    (classT 'posn3D 'posn
            (list (fieldT 'z (numT)))
            (list (methodT 'mdist (numT) (numT)
                           (plusI (getI (thisI) 'z) 
                                  (superI 'mdist (argI)))))))

  (define square-t-class 
    (classT 'square 'object
            (list (fieldT 'topleft (objT 'posn)))
            (list)))

  (define (typecheck-posn a)
    (typecheck a
               (list posn-t-class posn3D-t-class square-t-class)))
  
  (define posn27 (newI 'posn (list (numI 2) (numI 7))))
  (define posn531 (newI 'posn3D (list (numI 5) (numI 3) (numI 1))))

  (test (typecheck-posn (sendI posn27 'mdist (numI 0)))
        (numT))
  (test (typecheck-posn (sendI posn531 'mdist (numI 0)))
        (numT))  
  (test (typecheck-posn (sendI posn531 'addDist posn27))
        (numT))  
  (test (typecheck-posn (sendI posn27 'addDist posn531))
        (numT))

  (test (typecheck-posn (newI 'square (list (newI 'posn (list (numI 0) (numI 1))))))
        (objT 'square))
  (test (typecheck-posn (newI 'square (list (newI 'posn3D (list (numI 0) (numI 1) (numI 3))))))
        (objT 'square))
  
  (test (typecheck (multI (numI 1) (numI 2))
                   empty)
        (numT))

  (test/exn (typecheck-posn (sendI (numI 10) 'mdist (numI 0)))
            "no type")
  (test/exn (typecheck-posn (sendI posn27 'mdist posn27))
            "no type")
  (test/exn (typecheck (plusI (numI 1) (newI 'object empty))
                       empty)
            "no type")
  (test/exn (typecheck (plusI (newI 'object empty) (numI 1))
                       empty)
            "no type")
  (test/exn (typecheck (plusI (numI 1) (newI 'object (list (numI 1))))
                       empty)
            "no type")
  (test/exn (typecheck (getI (numI 1) 'x)
                       empty)
            "no type")
  (test/exn (typecheck (numI 10)
                       (list posn-t-class 
                             (classT 'other 'posn
                                     (list)
                                     (list (methodT 'mdist 
                                                    (objT 'object) (numT)
                                                    (numI 10))))))
            "bad override")
  (test/exn (typecheck-method (methodT 'm (numT) (objT 'object) (numI 0)) (objT 'object) empty)
            "no type")
  (test/exn (typecheck (numI 0)
                       (list square-t-class
                             (classT 'cube 'square
                                     empty
                                     (list
                                      (methodT 'm (numT) (numT)
                                               ;; No such method in superclass:
                                               (superI 'm (numI 0)))))))
            "not found"))

;; ----------------------------------------

(define strip-types : (ClassT -> ClassI)
  (lambda (t-class)
    (type-case ClassT t-class
      [classT (name super-name fields methods)
              (classI
               name 
               super-name
               (map fieldT-name fields)
               (map (lambda (m)
                      (type-case MethodT m
                        [methodT (name arg-type result-type body-expr)
                                 (methodI name body-expr)]))
                    methods))])))

(define interp-t : (ExprI (listof ClassT) -> Value)
  (lambda (a t-classes)
    (interp-i a
              (map strip-types t-classes))))

(module+ test
  (define (interp-t-posn a)
    (interp-t a
              (list posn-t-class posn3D-t-class))) 
  
  (test (interp-t-posn (sendI posn27 'mdist (numI 0))) 
        (numV 9))  
  (test (interp-t-posn (sendI posn531 'mdist (numI 0)))
        (numV 9))
  (test (interp-t-posn (sendI posn531 'addDist posn27))
        (numV 18))
  (test (interp-t-posn (sendI posn27 'addDist posn531))
        (numV 18))





  ;; NEW TESTS-----------------------------------------
  
  ;; this/arg
  (test/exn (typecheck (thisI) empty)
            "this in main exp")
  (test/exn (typecheck (argI) empty)
            "arg in main exp")

  ;; if0
  (test (typecheck (if0I (numI 0) (numI 1) (numI 2)) empty)
        (numT))
  (test (typecheck-posn (if0I (numI 0)
                              (newI 'square (list (newI 'posn (list (numI 0) (numI 1)))))
                              (newI 'square (list (newI 'posn (list (numI 5) (numI 6)))))))
        (objT 'square))
  (test (typecheck-posn (if0I (numI 0)
                              (newI 'posn (list (numI 0) (numI 1)))
                              (newI 'posn3D (list (numI 5) (numI 6) (numI 7)))))
        (objT 'posn))
  (test (typecheck-posn (if0I (numI 0)                            
                              (newI 'posn3D (list (numI 5) (numI 6) (numI 7)))
                              (newI 'posn (list (numI 0) (numI 1)))))
        (objT 'posn))
  (test (typecheck-posn (if0I (numI 0)
                              (newI 'posn (list (numI 0) (numI 1)))
                              (newI 'square (list (newI 'posn (list (numI 0) (numI 1)))))))
        (objT 'object))   
  (test/exn (typecheck-posn (if0I (numI 0)
                                  (newI 'square (list (newI 'posn (list (numI 0) (numI 1)))))
                                  (numI 2)))
            "no type")  
  (test/exn (typecheck-posn (if0I (numI 0)
                                  (numI 2)
                                  (newI 'square (list (newI 'posn (list (numI 0) (numI 1)))))))
            "no type")   
  (test/exn (typecheck-posn (if0I (newI 'square (list (newI 'posn (list (numI 0) (numI 1)))))
                                  (numI 1) (numI 2)))
            "no type")

  ;; instanceof
  (test (typecheck-posn (instanceofI (newI 'square (list (newI 'posn (list (numI 0) (numI 1)))))
                                     'square))
        (numT))
  (test/exn (typecheck-posn (instanceofI (numI 1)
                                         'square))
            "no type")

  ;; cast
  (test (typecheck-posn (castI 'posn (newI 'posn3D (list (numI 5) (numI 6) (numI 7)))))
        (objT 'posn))
  (test (typecheck-posn (castI 'posn (newI 'posn (list (numI 5) (numI 6)))))
        (objT 'posn))
  (test (typecheck-posn (castI 'posn3D (newI 'posn (list (numI 5) (numI 6)))))
        (objT 'posn3D))
  (test/exn (typecheck-posn (castI 'square (newI 'posn3D (list (numI 5) (numI 6) (numI 7)))))
            "no type")
  (test/exn (typecheck-posn (castI 'square (numI 1)))
            "no type")

  ;; null
  (test (typecheck-posn (nullI))
        (nullT))
  (test (typecheck-posn (if0I (numI 0)
                              (nullI)
                              (nullI)))
        (nullT))
  (test/exn (typecheck-posn (if0I (numI 0)
                                  (nullI)
                                  (numI 0)))
            "no type")
  (test/exn (typecheck-posn (plusI (numI 0) (nullI)))
            "no type")
  (test/exn (typecheck-posn (getI (nullI) 'x))
            "no type")
  (test/exn (typecheck-posn (sendI (nullI) 'm (numI 0)))
            "no type")
  
  (define nulltest-t-class
    (classT 'nulltest 'object
            (list (fieldT 'x (nullT)))
            (list (methodT 'trynull (nullT) (nullT) 
                           (getI (thisI) 'x)))))

  (test (typecheck (sendI (newI 'nulltest (list (nullI))) 'trynull (nullI))
                   (list nulltest-t-class))
        (nullT))
  
  (test (typecheck-posn (newI 'square (list (nullI))))
        (objT 'square))
  
  (define nulltest2-t-class
    (classT 'nulltest2 'object
            (list (fieldT 'x (objT 'posn)))
            (list (methodT 'trynull2 (objT 'posn) (objT 'posn) 
                           (getI (thisI) 'x)))))
  
  (test (typecheck (sendI (newI 'nulltest2 (list posn27)) 'trynull2 (nullI))
                   (list nulltest2-t-class posn-t-class posn3D-t-class square-t-class))
        (objT 'posn)) 

  (test/exn (typecheck-posn (newI 'posn (list (nullI) (nullI))))
        "no type")

  (define nulltest3-t-class
    (classT 'nulltest3 'object
            (list (fieldT 'x (numT)))
            (list (methodT 'trynull3 (numT) (numT) 
                           (getI (thisI) 'x)))))
  
  (test/exn (typecheck (sendI (newI 'nulltest3 (list (numI 1))) 'trynull3 (nullI))
                       (list nulltest3-t-class))
            "no type")
  
   
  )