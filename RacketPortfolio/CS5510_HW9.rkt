#lang plai-typed
(require plai-typed/s-exp-match)

;; Andrew Katsanevas
;; 11/9/2016
;; 5510 HW 9

(define-type Value
  [numV (n : number)]
  [boolV (b : boolean)]
  [closV (arg : (listof symbol))
         (body : ExprC)
         (env : Env)]
  [consV (l : Value)
         (r : Value)])

(define-type ExprC
  [numC (n : number)]
  [idC (s : symbol)]
  [plusC (l : ExprC) 
         (r : ExprC)]
  [multC (l : ExprC)
         (r : ExprC)]
  [lamC (n : (listof symbol))
        (arg-type : (listof Type)) 
        (body : ExprC)]
  [appC (fun : ExprC)
        (arg : (listof ExprC))]
  [boolC (b : boolean)]
  [eqC (l : ExprC)
       (r : ExprC)]
  [ifC (con : ExprC)
       (thn : ExprC)
       (els : ExprC)]
  [consC (l : ExprC)
         (r : ExprC)]
  [firstC (arg : ExprC)]
  [restC (arg : ExprC)])

(define-type Type
  [numT]
  [boolT]
  [arrowT (arg : (listof Type))
          (result : Type)]
  [crossT (l : Type)
          (r : Type)])

(define-type Binding
  [bind (name : symbol)
        (val : Value)])

(define-type-alias Env (listof Binding))

(define-type TypeBinding
  [tbind (name : symbol)
         (type : Type)])

(define-type-alias TypeEnv (listof TypeBinding))

(define mt-env empty)
(define extend-env cons)

(module+ test
  (print-only-errors true))

;; parse ----------------------------------------
(define (parse [s : s-expression]) : ExprC
  (cond
    [(s-exp-match? `NUMBER s) (numC (s-exp->number s))]
    [(s-exp-match? `true s)
     (boolC true)] 
    [(s-exp-match? `false s)
     (boolC false)]
    [(s-exp-match? `SYMBOL s) (idC (s-exp->symbol s))]
    [(s-exp-match? '{+ ANY ANY} s)
     (plusC (parse (second (s-exp->list s)))
            (parse (third (s-exp->list s))))]
    [(s-exp-match? '{* ANY ANY} s)
     (multC (parse (second (s-exp->list s)))
            (parse (third (s-exp->list s))))]
    [(s-exp-match? '{let {[SYMBOL : ANY ANY]} ANY} s)
     (let ([bs (s-exp->list (first
                             (s-exp->list (second
                                           (s-exp->list s)))))])
       (appC (lamC (list (s-exp->symbol (first bs)))
                   (list (parse-type (third bs)))
                   (parse (third (s-exp->list s))))
             (list (parse (fourth bs)))))]
    [(s-exp-match? '{lambda {[SYMBOL : ANY] ...} ANY} s)
     (let ([args (s-exp->list (second (s-exp->list s)))]) 
       (lamC (map s-exp->symbol (map first (map s-exp->list args)))
             (map parse-type (map third (map s-exp->list args)))
             (parse (third (s-exp->list s)))))] 
    [(s-exp-match? '{= ANY ANY} s)
     (eqC (parse (second (s-exp->list s)))
           (parse (third (s-exp->list s))))]
    [(s-exp-match? '{if ANY ANY ANY} s)
     (ifC (parse (second (s-exp->list s)))
          (parse (third (s-exp->list s)))
          (parse (fourth (s-exp->list s))))]
    [(s-exp-match? '{cons ANY ANY} s)
     (consC (parse (second (s-exp->list s)))
            (parse (third (s-exp->list s))))]
    [(s-exp-match? '{first ANY} s)
     (firstC (parse (second (s-exp->list s))))]
    [(s-exp-match? '{rest ANY} s)
     (restC (parse (second (s-exp->list s))))]
    [(s-exp-match? '{ANY ANY ...} s)
     (type-case ExprC (parse (first (s-exp->list s)))
       [plusC (l r) (error 'parse "invalid input")]
       [else (appC (parse (first (s-exp->list s)))
           (map parse (rest (s-exp->list s))))])]))

(define (parse-type [s : s-expression]) : Type
  (cond 
   [(s-exp-match? `num s) 
    (numT)]
   [(s-exp-match? `bool s)
    (boolT)]
   [(s-exp-match? `(ANY ... -> ANY) s)
    (arrowT (list (parse-type (first (s-exp->list s))))
            (parse-type (third (s-exp->list s))))]
   [(s-exp-match? `(ANY * ANY) s)
    (crossT (parse-type (first (s-exp->list s)))
            (parse-type (third (s-exp->list s))))]
   [else (error 'parse-type "invalid input")]))

(module+ test
  (test (parse '2)
        (numC 2))
  (test (parse `x) ; note: backquote instead of normal quote
        (idC 'x))
  (test (parse '{+ 2 1})
        (plusC (numC 2) (numC 1)))
  (test (parse '{* 3 4})
        (multC (numC 3) (numC 4)))
  (test (parse '{+ {* 3 4} 8})
        (plusC (multC (numC 3) (numC 4))
               (numC 8)))
  (test (parse '{let {[x : num {+ 1 2}]}
                  y})
        (appC (lamC (list 'x) (list (numT)) (idC 'y))
              (list (plusC (numC 1) (numC 2)))))
  (test (parse '{lambda {[x : num]} 9})
        (lamC (list 'x) (list (numT)) (numC 9)))
  (test (parse '(= 1 2))
        (eqC (numC 1) (numC 2)))
  (test (parse '(if true 1 2))
        (ifC (boolC true) (numC 1) (numC 2)))
  (test (parse '(cons 1 2))
        (consC (numC 1) (numC 2)))
  (test (parse '(first (cons 1 2)))
        (firstC (consC (numC 1) (numC 2))))
  (test (parse '(rest (cons 1 2)))
        (restC (consC (numC 1) (numC 2))))
  (test (parse '{double 9})
        (appC (idC 'double) (list (numC 9))))
  (test/exn (parse '{{+ 1 2}})  
            "invalid input")

  (test (parse-type `num)
        (numT))
  (test (parse-type `bool)
        (boolT))
  (test (parse-type `(num -> bool))
        (arrowT (list (numT)) (boolT)))
  (test/exn (parse-type '1)
            "invalid input"))

;; interp ----------------------------------------
(define (interp [a : ExprC] [env : Env]) : Value
  (type-case ExprC a
    [numC (n) (numV n)]
    [idC (s) (lookup s env)]
    [plusC (l r) (num+ (interp l env) (interp r env))]
    [multC (l r) (num* (interp l env) (interp r env))]
    [lamC (n t body)
          (closV n body env)]
    [appC (fun arg) (type-case Value (interp fun env)
                      [closV (n body c-env)
                             (interp body (interp-args-env n arg env c-env))]
                      [else (error 'interp "not a function")])]
    [boolC (b) (boolV b)]
    [eqC (l r) (type-case Value (interp l env)
                 [numV (n-l) (type-case Value (interp r env)
                               [numV (n-r) (boolV (= n-l n-r))]
                               [else (error 'interp "not a number")])]
                 [else (error 'interp "not a number")])] 
    [ifC (con thn els) (type-case Value (interp con env)
                         [boolV (b) (cond
                                      [(eq? b #t) (interp thn env)]
                                      [(eq? b #f) (interp els env)])]
                         [else (error 'interp "not a boolean")])]
    [consC (l r) (consV (interp l env) (interp r env))]
    [firstC (arg) (type-case Value (interp arg env)
                    [consV (l r) l]
                    [else (error 'interp "not a cons")])]
    [restC (arg) (type-case Value (interp arg env)
                    [consV (l r) r]
                    [else (error 'interp "not a cons")])]))


(define (interp-args-env [n : (listof symbol)] [args : (listof ExprC)] [env : Env] [newenv : Env]) : Env
  (cond
    [(empty? args) newenv]
    [else (interp-args-env (rest n) (rest args) env (extend-env (bind (first n) (interp (first args) env)) newenv))]))

(module+ test
  (test (interp (parse '2) mt-env)
        (numV 2))
  (test/exn (interp (parse `x) mt-env)
            "free variable")
  (test (interp (parse `x) 
                (extend-env (bind 'x (numV 9)) mt-env))
        (numV 9))
  (test (interp (parse '{+ 2 1}) mt-env)
        (numV 3))
  (test (interp (parse '{* 2 1}) mt-env)
        (numV 2))
  (test (interp (parse '{+ {* 2 3} {+ 5 8}})
                mt-env)
        (numV 19))
  (test (interp (parse '{lambda {[x : num]} {+ x x}})
                mt-env)
        (closV (list 'x) (plusC (idC 'x) (idC 'x)) mt-env))
  (test (interp (parse '{let {[x : num 5]}
                          {+ x x}})
                mt-env)
        (numV 10))
  (test (interp (parse '{let {[x : num 5]}
                          {let {[x : num {+ 1 x}]}
                            {+ x x}}})
                mt-env)
        (numV 12))
  (test (interp (parse '{let {[x : num 5]}
                          {let {[y : num 6]}
                            x}})
                mt-env)
        (numV 5))
  (test (interp (parse '{{lambda {[x : num]} {+ x x}} 8})
                mt-env)
        (numV 16))

  (test (interp (parse '(if true 1 2)) mt-env)
        (numV 1))

  (test (interp (parse '(if false 1 2)) mt-env)
        (numV 2))

  (test/exn (interp (parse '{1 2}) mt-env)
            "not a function")
  (test/exn (interp (parse '{+ 1 {lambda {[x : num]} x}}) mt-env)
            "not a number")
  (test/exn (interp (parse '{let {[bad : (num -> num) {lambda {[x : num]} {+ x y}}]}
                              {let {[y : num 5]}
                                {bad 2}}})
                    mt-env)
            "free variable"))

;; num+ and num* ----------------------------------------
(define (num-op [op : (number number -> number)] [l : Value] [r : Value]) : Value
  (cond
   [(and (numV? l) (numV? r))
    (numV (op (numV-n l) (numV-n r)))]
   [else
    (error 'interp "not a number")]))
(define (num+ [l : Value] [r : Value]) : Value
  (num-op + l r))
(define (num* [l : Value] [r : Value]) : Value
  (num-op * l r))

(module+ test
  (test (num+ (numV 1) (numV 2))
        (numV 3))
  (test (num* (numV 2) (numV 3))
        (numV 6)))

;; lookup ----------------------------------------
(define (make-lookup [name-of : ('a -> symbol)] [val-of : ('a -> 'b)])
  (lambda ([name : symbol] [vals : (listof 'a)]) : 'b
    (cond
     [(empty? vals)
      (error 'find "free variable")]
     [else (if (equal? name (name-of (first vals)))
               (val-of (first vals))
               ((make-lookup name-of val-of) name (rest vals)))])))

(define lookup
  (make-lookup bind-name bind-val))

(module+ test
  (test/exn (lookup 'x mt-env)
            "free variable")
  (test (lookup 'x (extend-env (bind 'x (numV 8)) mt-env))
        (numV 8))
  (test (lookup 'x (extend-env
                    (bind 'x (numV 9))
                    (extend-env (bind 'x (numV 8)) mt-env)))
        (numV 9))
  (test (lookup 'y (extend-env
                    (bind 'x (numV 9))
                    (extend-env (bind 'y (numV 8)) mt-env)))
        (numV 8)))

;; typecheck ----------------------------------------
(define (typecheck [a : ExprC] [tenv : TypeEnv])
  (type-case ExprC a
    [numC (n) (numT)]
    [plusC (l r) (typecheck-nums l r tenv)]
    [multC (l r) (typecheck-nums l r tenv)]
    [idC (n) (type-lookup n tenv)]
    [lamC (n arg-type body)
          (arrowT arg-type
                  (typecheck body 
                             (extend-env-list (map2 tbind n arg-type)
                                         tenv)))]
    [appC (fun arg)
          (type-case Type (typecheck fun tenv)
            [arrowT (arg-type result-type)
                    (if (typecheck-list arg tenv arg-type)
                        result-type
                        (type-error arg
                                    (to-string arg-type)))]
            [else (type-error fun "function")])]
    [boolC (b) (boolT)]
    [eqC (l r) (typecheck-nums-eq l r tenv)]
    [ifC (con thn els) (type-case Type (typecheck con tenv)
                         [boolT () (cond
                                  [(equal? (typecheck thn tenv) (typecheck els tenv)) (typecheck thn tenv)]
                                  [else (type-error els (to-string (typecheck thn tenv)))])]
                         [else (type-error con "boolean")])]
    [consC (l r) (crossT (typecheck l tenv) (typecheck r tenv))]
    [firstC (arg) (type-case Type (typecheck arg tenv)
                    [crossT (l r) l]
                    [else (type-error arg "cons")])]
    [restC (arg) (type-case Type (typecheck arg tenv)
                    [crossT (l r) r]
                    [else (type-error arg "cons")])]))

(define (extend-env-list [tbinds : (listof TypeBinding)] [tenv : TypeEnv])
  (cond
    [(empty? tbinds) tenv]
    [else (extend-env-list (rest tbinds) (extend-env (first tbinds) tenv))]))

(define (typecheck-list [args : (listof ExprC)] [tenv : TypeEnv] [arg-type : (listof Type)]) : boolean
  (cond
    [(empty? args) #t]
    [(equal? (typecheck (first args) tenv) (first arg-type)) (typecheck-list (rest args) tenv (rest arg-type))]
    [else #f]))

(define (typecheck-nums-eq l r tenv)
  (type-case Type (typecheck l tenv)
    [numT ()
          (type-case Type (typecheck r tenv)
            [numT () (boolT)]
            [else (type-error r "num")])]
    [else (type-error l "num")]))

(define (typecheck-nums l r tenv)
  (type-case Type (typecheck l tenv)
    [numT ()
          (type-case Type (typecheck r tenv)
            [numT () (numT)]
            [else (type-error r "num")])]
    [else (type-error l "num")]))

(define (type-error a msg)
  (error 'typecheck (string-append
                     "no type: "
                     (string-append
                      (to-string a)
                      (string-append " not "
                                     msg)))))

(define type-lookup
  (make-lookup tbind-name tbind-type))

(module+ test
  (test (typecheck (parse '10) mt-env)
        (numT))
  (test (typecheck (parse '{+ 10 17}) mt-env)
        (numT))
  (test (typecheck (parse '{* 10 17}) mt-env)
        (numT))
  (test (typecheck (parse '{lambda {[x : num]} 12}) mt-env)
        (arrowT (list (numT)) (numT)))
  (test (typecheck (parse '{lambda {[x : num]} {lambda {[y : bool]} x}}) mt-env)
        (arrowT (list (numT)) (arrowT (list (boolT))  (numT))))

  (test (typecheck (parse '{{lambda {[x : num]} 12}
                            {+ 1 17}})
                   mt-env)
        (numT))

  (test (typecheck (parse '{let {[x : num 4]}
                             {let {[f : (num -> num)
                                      {lambda {[y : num]} {+ x y}}]}
                               {f x}}})
                   mt-env)
        (numT))

  (test/exn (typecheck (parse '{1 2})
                       mt-env)
            "no type")
  (test/exn (typecheck (parse '{{lambda {[x : bool]} x} 2})
                       mt-env)
            "no type")
  (test/exn (typecheck (parse '{+ 1 {lambda {[x : num]} x}})
                       mt-env)
            "no type")
  (test/exn (typecheck (parse '{* {lambda {[x : num]} x} 1})
                       mt-env)
            "no type")

  ;; Part 1
  (test (interp (parse '{if {= 13 {if {= 1 {+ -1 2}}
                                      12
                                      13}}
                            4
                            5})
                mt-env)
         (numV 5))

  (test (typecheck (parse '{= 13 {if {= 1 {+ -1 2}}
                                     12
                                     13}})
                   mt-env)
         (boolT)) 

  (test/exn (typecheck (parse '{+ 1 {if true true false}})
                       mt-env)
            "no type")

  ;; Part 2
  (test (interp (parse '{cons 10 8})
                mt-env)
        ;; Your constructor might be different than consV:
        (consV (numV 10) (numV 8)))
  
  (test (interp (parse '{first {cons 10 8}})
                mt-env)
        (numV 10))
  
  (test (interp (parse '{rest {cons 10 8}})
                mt-env)
        (numV 8))
  
  (test (typecheck (parse '{cons 10 8})
                   mt-env)
        ;; Your constructor might be different than crossT:
        (crossT (numT) (numT)))
  
  (test (typecheck (parse '{first {cons 10 8}})
                   mt-env)
        (numT))
  
  (test (typecheck (parse '{+ 1 {rest {cons 10 8}}})
                   mt-env)
        (numT))
  
  (test (typecheck (parse '{lambda {[x : (num * bool)]}
                             {first x}})
                   mt-env)
        ;; Your constructor might be different than crossT:
        (arrowT (list (crossT (numT) (boolT))) (numT)))
  
  (test (typecheck (parse '{{lambda {[x : (num * bool)]}
                              {first x}}
                            {cons 1 false}})
                   mt-env)
        (numT))
  
  (test (typecheck (parse '{{lambda {[x : (num * bool)]}
                              {rest x}}
                            {cons 1 false}})
                   mt-env)
        (boolT))
  
  (test/exn (typecheck (parse '{first 10})
                       mt-env)
            "no type")
  
  (test/exn (typecheck (parse '{+ 1 {first {cons false 8}}})
                       mt-env)
            "no type")
  
  (test/exn (typecheck (parse '{lambda {[x : (num * bool)]}
                                 {if {first x}
                                     1
                                     2}})
                       mt-env)
            "no type")

  ;; Part 3
  (test (interp (parse '{{lambda {}
                           10}})
                mt-env)
        (numV 10))
  
  (test (interp (parse '{{lambda {[x : num] [y : num]} {+ x y}}
                         10
                         20})
                mt-env)
        (numV 30))
  
  
  (test (typecheck (parse '{{lambda {[x : num] [y : bool]} y}
                            10
                            false})
                   mt-env)
        (boolT))
  
  (test/exn (typecheck (parse '{{lambda {[x : num] [y : bool]} y}
                                false
                                10})
                       mt-env)
            "no type")

  ;; New Exceptions
  (test/exn (interp (parse '(= true 1)) mt-env)
            "not a number")

  (test/exn (interp (parse '(= 1 false)) mt-env)
            "not a number")

  (test/exn (interp (parse '(if 1 42 12345)) mt-env)
            "not a boolean")
  
  (test/exn (interp (parse '(first 1)) mt-env)
            "not a cons")

  (test/exn (interp (parse '(rest 1)) mt-env)
            "not a cons")

  (test/exn (typecheck (parse '(if true 1 false)) mt-env)
            "no type")

  (test/exn (typecheck (parse '(first 1)) mt-env)
            "no type")

  (test/exn (typecheck (parse '(rest 1)) mt-env)
            "no type")

  (test/exn (typecheck-nums-eq (numC 1) (boolC #f) mt-env)
            "no type")

  (test/exn (typecheck-nums-eq (boolC #f) (numC 1) mt-env)
            "no type")
  
  )