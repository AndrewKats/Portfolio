#lang plai-typed

(define-type ExprC
  [numC (n : number)]
  [plusC (lhs : ExprC)
         (rhs : ExprC)]
  [multC (lhs : ExprC)
         (rhs : ExprC)]
  [argC]
  [thisC]
  [nullC]
  [newC (class-name : symbol)
        (args : (listof ExprC))]
  [getC (obj-expr : ExprC)
        (field-name : symbol)]
  [sendC (obj-expr : ExprC)
         (method-name : symbol)
         (arg-expr : ExprC)]
  [ssendC (obj-expr : ExprC)
          (class-name : symbol)
          (method-name : symbol)
          (arg-expr : ExprC)]
  [if0C (con : ExprC)
        (thn : ExprC)
        (els : ExprC)]
  [instanceofC (obj : ExprC)
               (cname : symbol)]
  [castC (cname : symbol)
         (exp : ExprC)])

(define-type ClassC
  [classC (name : symbol)
          (super-name : symbol)
          (field-names : (listof symbol))
          (methods : (listof MethodC))])

(define-type MethodC
  [methodC (name : symbol)
           (body-expr : ExprC)])

(define-type Value
  [numV (n : number)]
  [objV (class-name : symbol)
        (field-values : (listof Value))]
  [nullV])

(module+ test
  (print-only-errors true))

;; ----------------------------------------

(define (make-find [name-of : ('a -> symbol)])
  (lambda ([name : symbol] [vals : (listof 'a)]) : 'a
    (cond
     [(empty? vals)
      (error 'find "not found")]
     [else (if (equal? name (name-of (first vals)))
               (first vals)
               ((make-find name-of) name (rest vals)))])))

(define find-class : (symbol (listof ClassC) -> ClassC)
  (make-find classC-name))

(define find-method : (symbol (listof MethodC) -> MethodC)
  (make-find methodC-name))

;; A non-list pair:
(define-type (Pair 'a 'b)
  [kons (first : 'a) (rest : 'b)])

(define (get-field [name : symbol] 
                   [field-names : (listof symbol)] 
                   [vals : (listof Value)])
  ;; Pair fields and values, find by field name,
  ;; then extract value from pair
  (kons-rest ((make-find kons-first)
              name
              (map2 kons field-names vals))))

(module+ test
  (test/exn (find-class 'a empty)
            "not found")
  (test (find-class 'a (list (classC 'a 'object empty empty)))
        (classC 'a 'object empty empty))
  (test (find-class 'b (list (classC 'a 'object empty empty)
                             (classC 'b 'object empty empty)))
        (classC 'b 'object empty empty))
  (test (get-field 'a 
                   (list 'a 'b)
                   (list (numV 0) (numV 1)))
        (numV 0)))

;; ----------------------------------------

(define interp : (ExprC (listof ClassC) Value Value -> Value)
  (lambda (a classes this-val arg-val)
    (local [(define (recur expr)
              (interp expr classes this-val arg-val))]
      (type-case ExprC a
        [numC (n) (numV n)]
        [plusC (l r) (num+ (recur l) (recur r))]
        [multC (l r) (num* (recur l) (recur r))]
        [thisC () this-val]
        [argC () arg-val]
        [newC (class-name field-exprs)
              (local [(define c (find-class class-name classes))
                      (define vals (map recur field-exprs))]
                (if (= (length vals) (length (classC-field-names c)))
                    (objV class-name vals)
                    (error 'interp "wrong field count")))]
        [getC (obj-expr field-name)
              (type-case Value (recur obj-expr)
                [objV (class-name field-vals)
                      (type-case ClassC (find-class class-name classes)
                        [classC (name super-name field-names methods)
                                (get-field field-name field-names 
                                           field-vals)])]
                [else (error 'interp "not an object")])]
        [sendC (obj-expr method-name arg-expr)
               (local [(define obj (recur obj-expr))
                       (define arg-val (recur arg-expr))]
                 (type-case Value obj
                   [objV (class-name field-vals)
                         (call-method class-name method-name classes
                                      obj arg-val)]
                   [else (error 'interp "not an object")]))]
        [ssendC (obj-expr class-name method-name arg-expr)
                (local [(define obj (recur obj-expr))
                        (define arg-val (recur arg-expr))]
                  (call-method class-name method-name classes
                               obj arg-val))]
        [if0C (con thn els)
              (type-case Value (interp con classes this-val arg-val)
                [numV (n) (cond
                            [(= n 0) (interp thn classes this-val arg-val)]
                            [else (interp els classes this-val arg-val)])]
                [else (error 'interp "not a number")])]
        [instanceofC (obj-expr class-name)
                     (type-case Value (recur obj-expr)
                       [objV (obj-class-name field-values)
                             (if (subclass? obj-class-name class-name classes)
                                 (numV 0)
                                 (begin
                                   (find-class class-name classes)
                                   (numV 1)))]
                       [else (error 'interp "not an object")])]
        [castC (castname exp)
               (local [(define interped-obj (recur exp))]
                 (type-case Value interped-obj
                   [objV (classname fields) (cond
                                          [(subclass? classname castname classes) interped-obj]
                                          [else (error 'interp "not a subclass")])]
                   [else (error 'interp "not an object")]))]
        [nullC () (nullV)]))))

(define (subclass? obj-class-name class-name classes)
  (cond
    [(equal? obj-class-name class-name) #t]
    [(equal? obj-class-name 'object) #f]
    [else
     (type-case ClassC (find-class obj-class-name classes)
       [classC (name super-name field-names methods)
               (subclass? super-name class-name classes)])]))

(module+ test
  (test (subclass? 'object 'object empty)
        #t)
  (test (subclass? 'a 'object (list (classC 'a 'object empty empty)))
        #t)
  (test (subclass? 'object 'a (list (classC 'a 'object empty empty)))
        #f)
  (test (subclass? 'b 'a (list (classC 'a 'object empty empty)
                               (classC 'b 'a empty empty)))
        #t)
  (test (subclass? 'b 'object (list (classC 'a 'object empty empty)
                                    (classC 'b 'a empty empty)))
        #t))

(define (call-method class-name method-name classes
                     obj arg-val)
  (type-case ClassC (find-class class-name classes)
    [classC (name super-name field-names methods)
            (type-case MethodC (find-method method-name methods)
              [methodC (name body-expr)
                       (interp body-expr
                               classes
                               obj
                               arg-val)])]))

(define (num-op [op : (number number -> number)]
                [op-name : symbol] 
                [x : Value]
                [y : Value]) : Value
  (cond
    [(and (numV? x) (numV? y))
     (numV (op (numV-n x) (numV-n y)))]
    [else (error 'interp "not a number")]))

(define (num+ x y) (num-op + '+ x y))
(define (num* x y) (num-op * '* x y))

;; ----------------------------------------
;; Examples

(module+ test
  (define posn-class
    (classC 
     'posn
     'object
     (list 'x 'y)
     (list (methodC 'mdist
                    (plusC (getC (thisC) 'x) (getC (thisC) 'y)))
           (methodC 'addDist
                    (plusC (sendC (thisC) 'mdist (numC 0))
                           (sendC (argC) 'mdist (numC 0))))
           (methodC 'addX
                    (plusC (getC (thisC) 'x) (argC)))
           (methodC 'multY (multC (argC) (getC (thisC) 'y)))
           (methodC 'factory12 (newC 'posn (list (numC 1) (numC 2)))))))

  (define posn3D-class
    (classC 
     'posn3D
     'posn
     (list 'x 'y 'z)
     (list (methodC 'mdist (plusC (getC (thisC) 'z)
                                  (ssendC (thisC) 'posn 'mdist (argC))))
           (methodC 'addDist (ssendC (thisC) 'posn 'addDist (argC))))))

  (define posn27 (newC 'posn (list (numC 2) (numC 7))))
  (define posn531 (newC 'posn3D (list (numC 5) (numC 3) (numC 1))))

  (define (interp-posn a)
    (interp a (list posn-class posn3D-class) (numV -1) (numV -1))))

;; ----------------------------------------

(module+ test
  (test (interp (numC 10) 
                empty (numV -1) (numV -1))
        (numV 10))
  (test (interp (plusC (numC 10) (numC 17))
                empty (numV -1) (numV -1))
        (numV 27))
  (test (interp (multC (numC 10) (numC 7))
                empty (numV -1) (numV -1))
        (numV 70))

  (test (interp-posn (newC 'posn (list (numC 2) (numC 7))))
        (objV 'posn (list (numV 2) (numV 7))))

  (test (interp-posn (sendC posn27 'mdist (numC 0)))
        (numV 9))
  
  (test (interp-posn (sendC posn27 'addX (numC 10)))
        (numV 12))

  (test (interp-posn (sendC (ssendC posn27 'posn 'factory12 (numC 0))
                            'multY
                            (numC 15)))
        (numV 30))

  (test (interp-posn (sendC posn531 'addDist posn27))
        (numV 18))
  
  (test/exn (interp-posn (plusC (numC 1) posn27))
            "not a number")
  (test/exn (interp-posn (getC (numC 1) 'x))
            "not an object")
  (test/exn (interp-posn (sendC (numC 1) 'mdist (numC 0)))
            "not an object")
  (test/exn (interp-posn (ssendC (numC 1) 'posn 'mdist (numC 0)))
            "not an object")
  (test/exn (interp-posn (newC 'posn (list (numC 0))))
            "wrong field count")

  ;; NEW TESTS --------------------------------------------
  
  ;; if0
  (test (interp (if0C (numC 0) (numC 1) (numC 2)) empty (numV -1) (numV -1))
            (numV 1))
  (test (interp (if0C (numC 1) (numC 1) (numC 2)) empty (numV -1) (numV -1))
            (numV 2))
  (test/exn (interp-posn (if0C (newC 'posn (list (numC 2) (numC 7))) (numC 1) (numC 2)))
            "not a number")

  ;; instanceof
  (test (interp-posn (instanceofC (newC 'posn3D (list (numC 2) (numC 7) (numC 16))) 'posn))
        (numV 0))
  (test (interp-posn (instanceofC (newC 'posn3D (list (numC 2) (numC 7) (numC 16))) 'object))
        (numV 0))
  (test (interp-posn (instanceofC (newC 'posn (list (numC 2) (numC 7))) 'posn3D))
        (numV 1))
  (test/exn (interp-posn (instanceofC (numC 1) 'posn3D))
        "not an object")

  ;; cast
  (test (interp-posn (castC 'posn (newC 'posn3D (list (numC 2) (numC 7) (numC 16)))))
        (objV 'posn3D (list (numV 2) (numV 7) (numV 16))))
  (test (interp-posn (castC 'posn (newC 'posn (list (numC 2) (numC 7)))))
        (objV 'posn (list (numV 2) (numV 7))))
  (test (interp-posn (castC 'object (newC 'posn3D (list (numC 2) (numC 7) (numC 16)))))
        (objV 'posn3D (list (numV 2) (numV 7) (numV 16))))
  (test/exn (interp-posn (castC 'posn3D (newC 'posn (list (numC 2) (numC 7)))))
        "not a subclass")
  (test/exn (interp-posn (castC 'posn3D (numC 1)))
        "not an object")

  ;; null
  (define test-class
    (classC 
     'testclass
     'object
     (list 'x)
     (list (methodC 'givenull (nullC)))))
  (test (interp-posn (nullC))
        (nullV))
  (test (interp (sendC (newC 'testclass (list (numC -1))) 'givenull (numC 0)) (list test-class) (numV -1) (numV -1))
        (nullV))
  (test/exn (interp-posn (getC (nullC) 'x))
            "not an object")
  (test/exn (interp-posn (sendC (nullC) 'mdist (numC 0)))
            "not an object")
  (test/exn (interp-posn (ssendC (nullC) 'posn 'mdist (numC 0)))
            "not an object")
  
  )