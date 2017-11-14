#lang plai-typed

;; Andrew Katsanevas
;; 9/21/2016
;; CS5510 HW4

(require plai-typed/s-exp-match)

(define-type-alias Location number)

(define-type Value
  [numV (n : number)]
  [closV (arg : symbol)
         (body : ExprC)
         (env : Env)]
  [boxV (l : Location)]
  [recV (members : (listof symbol))
        (locs : (listof Location))])

(define-type ExprC
  [numC (n : number)]
  [idC (s : symbol)]
  [plusC (l : ExprC) 
         (r : ExprC)] 
  [multC (l : ExprC)
         (r : ExprC)]
  [letC (n : symbol) 
        (rhs : ExprC)
        (body : ExprC)]
  [lamC (n : symbol)
        (body : ExprC)]
  [appC (fun : ExprC)
        (arg : ExprC)]
  [boxC (arg : ExprC)]
  [unboxC (arg : ExprC)]
  [setboxC (bx : ExprC)
           (val : ExprC)]
  [beginC (subs : (listof ExprC))
          (eval : ExprC)]
  [recordC (members : (listof symbol))
           (args : (listof ExprC))]
  [getC (record : ExprC)
        (member : symbol)]
  [setC (rec : ExprC)
        (n : symbol)
        (newVal : ExprC)]
  )

(define-type Binding
  [bind (name : symbol)
        (val : Value)])

(define-type-alias Env (listof Binding))

(define mt-env empty)
(define extend-env cons)

(define-type Storage
  [cell (location : Location) 
        (val : Value)])

(define-type-alias Store (listof Storage))
(define mt-store empty)
(define override-store cons)

(define-type Result
  [v*s (v : Value) (s : Store)])

(module+ test
  (print-only-errors true))

;; parse ----------------------------------------
(define (parse [s : s-expression]) : ExprC
  (cond
    [(s-exp-match? `NUMBER s) (numC (s-exp->number s))]
    [(s-exp-match? `SYMBOL s) (idC (s-exp->symbol s))]
    [(s-exp-match? '{+ ANY ANY} s)
     (plusC (parse (second (s-exp->list s)))
            (parse (third (s-exp->list s))))]
    [(s-exp-match? '{* ANY ANY} s)
     (multC (parse (second (s-exp->list s)))
            (parse (third (s-exp->list s))))]
    [(s-exp-match? '{let {[SYMBOL ANY]} ANY} s)
     (let ([bs (s-exp->list (first
                             (s-exp->list (second
                                           (s-exp->list s)))))])
       (letC (s-exp->symbol (first bs))
             (parse (second bs))
             (parse (third (s-exp->list s)))))]
    [(s-exp-match? '{lambda {SYMBOL} ANY} s)
     (lamC (s-exp->symbol (first (s-exp->list 
                                  (second (s-exp->list s)))))
           (parse (third (s-exp->list s))))]
    [(s-exp-match? '{box ANY} s)
     (boxC (parse (second (s-exp->list s))))]
    [(s-exp-match? '{unbox ANY} s)
     (unboxC (parse (second (s-exp->list s))))]
    [(s-exp-match? '{set-box! ANY ANY} s)
     (setboxC (parse (second (s-exp->list s)))
              (parse (third (s-exp->list s))))]
    [(s-exp-match? '{begin ANY ...} s)
     (cond
       [(> (length (rest (s-exp->list s))) 1)
        (beginC (map parse (reverse (rest (reverse (rest (s-exp->list s))))))
                (parse (first (reverse (rest (s-exp->list s))))))]
       [else (beginC empty (parse (first (rest (s-exp->list s)))))])]
    [(s-exp-match? '{record {SYMBOL ANY} ...} s)
     (recordC (map
               (lambda (memberList) (s-exp->symbol (first (s-exp->list memberList))))
               (rest (s-exp->list s)))
              (map
               (lambda (argList) (parse (second (s-exp->list argList))))
               (rest (s-exp->list s))))]
    [(s-exp-match? '{get ANY SYMBOL} s)
     (getC (parse (second (s-exp->list s)))
           (s-exp->symbol (third (s-exp->list s))))]
    [(s-exp-match? '{set ANY SYMBOL ANY} s)
     (setC (parse (second (s-exp->list s)))
           (s-exp->symbol (third (s-exp->list s)))
           (parse (fourth (s-exp->list s))))]
    [(s-exp-match? '{ANY ANY} s)
     (appC (parse (first (s-exp->list s)))
           (parse (second (s-exp->list s))))]
    [else (error 'parse "invalid input")]))

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
  (test (parse '{let {[x {+ 1 2}]}
                  y})
        (letC 'x (plusC (numC 1) (numC 2))
              (idC 'y)))
  (test (parse '{lambda {x} 9})
        (lamC 'x (numC 9)))
  (test (parse '{double 9})
        (appC (idC 'double) (numC 9)))
  (test (parse '{box 0})
        (boxC (numC 0)))
  (test (parse '{unbox b})
        (unboxC (idC 'b)))
  (test (parse '{set-box! b 0})
        (setboxC (idC 'b) (numC 0)))
  (test (parse '{begin 1 2})
        (beginC (cons (numC 1) empty) (numC 2)))
  
  (test (parse '(begin 1))
        (beginC empty (numC 1)))

  (test (parse '(record {a 1} {b 2}))
        (recordC (cons 'a (cons 'b empty)) (cons (numC 1) (cons (numC 2) empty))))

  (test (parse '{get {+ 1 2} a})
        (getC (plusC (numC 1) (numC 2)) 'a))

  (test (parse '{get {get {record {r {record {z 0}}}} r} z})
        (getC (getC (recordC (list 'r) (list (recordC (list 'z) (list (numC 0))))) 'r) 'z))
  (test/exn (parse '{{+ 1 2}})
            "invalid input"))

;; with form ----------------------------------------
(define-syntax-rule
  (with [(v-id sto-id) call]
    body)
  (type-case Result call
    [v*s (v-id sto-id) body]))
                                
;; interp ----------------------------------------
(define (interp [a : ExprC] [env : Env] [sto : Store]) : Result
  (type-case ExprC a
    [numC (n) (v*s (numV n) sto)]
    [idC (s) (v*s (lookup s env) sto)]
    [plusC (l r)
           (with [(v-l sto-l) (interp l env sto)]
             (with [(v-r sto-r) (interp r env sto-l)]
               (v*s (num+ v-l v-r) sto-r)))]
    [multC (l r)
           (with [(v-l sto-l) (interp l env sto)]
             (with [(v-r sto-r) (interp r env sto-l)]
               (v*s (num* v-l v-r) sto-r)))]
    [letC (n rhs body)
          (with [(v-rhs sto-rhs) (interp rhs env sto)]
            (interp body
                    (extend-env
                     (bind n v-rhs)
                     env)
                    sto-rhs))]
    [lamC (n body)
          (v*s (closV n body env) sto)]
    [appC (fun arg)
          (with [(v-f sto-f) (interp fun env sto)]
            (with [(v-a sto-a) (interp arg env sto-f)]
              (type-case Value v-f
                [closV (n body c-env)
                       (interp body
                               (extend-env
                                (bind n v-a)
                                c-env)
                               sto-a)]
                [else (error 'interp "not a function")])))]
    [boxC (a)
          (with [(v sto-v) (interp a env sto)]
            (let ([l (new-loc sto-v)])
              (v*s (boxV l) 
                   (override-store (cell l v) 
                                   sto-v))))]
    [unboxC (a)
            (with [(v sto-v) (interp a env sto)]
              (type-case Value v
                [boxV (l) (v*s (fetch l sto-v) 
                               sto-v)]
                [else (error 'interp "not a box")]))]
    [setboxC (bx val)
             (with [(v-b sto-b) (interp bx env sto)]
               (with [(v-v sto-v) (interp val env sto-b)]
                 (type-case Value v-b
                   [boxV (l)
                         (v*s v-v
                              (update-store (cell l v-v)
                                              sto-v empty sto-v))]
                   [else (error 'interp "not a box")])))]
    [beginC (l r)
            (interp r env (sub-store l env sto))]
    [recordC (members args)
             (recHelper members args empty env sto)]
    [getC (r m)
          (type-case Value (v*s-v (interp r env sto))
            [recV (ms ls) (v*s (find m ms ls (v*s-s (interp r env sto))) (v*s-s (interp r env sto)))]
            [else (error 'interp "not a record")])]
    [setC (rec n nv)
          (type-case Value (v*s-v (interp rec env sto))
            [recV (mems locs) (change-store (find-location mems locs n) (v*s-v (interp nv env sto)) empty sto)]
            [else (error 'interp "not a record")])]
    ))

(define (find-location [mems : (listof symbol)] [locs : (listof Location)] [n : symbol]) : Location
  (cond
    [(empty? mems) (error 'interp "field not found")]
    [(symbol=? n (first mems)) (first locs)]
    [else (find-location (rest mems) (rest locs) n)]))

(define (change-store [loc : Location] [newVal : Value] [beforeSto : Store] [sto : Store]) : Result
  (cond
    [(empty? sto)
     (error 'interp "field not found")]
    [(= loc (cell-location (first sto)))
     (v*s newVal (append beforeSto (override-store (cell loc newVal) (rest sto))))]
    [else
     (change-store loc newVal (append beforeSto (list (first sto))) (rest sto))]))

(define (recHelper [members : (listof symbol)] [args : (listof ExprC)] [locs : (listof Location)] [env : Env] [sto : Store]) : Result 
  (cond
    [(empty? args) (v*s (recV members locs) sto)]
    [else (with [(v-v sto-v) (interp (first args) env sto)]      
                (recHelper members (rest args) (append locs (list (next-location sto-v))) env (override-store (cell (next-location sto-v) v-v) sto-v)))])) 


(define (next-location [sto : Store]) : Location 
  (cond
    [(empty? sto) 1]
    [else (+ 1 (cell-location (first sto)))]))

(define (interp-expr [a : ExprC]) : s-expression
  (type-case Value (v*s-v (interp a empty empty))
    [numV (n) (number->s-exp n)]
    [closV (a b e) `function]
    [boxV (l) `box]
    [recV (m l) `record]))

(define (find [m : symbol] [ms : (listof symbol)] [ls : (listof Location)] [sto : Store]) : Value
  (cond
    [(empty? ms) (error 'interp "no such field")]
    [(symbol=? (first ms) m) (lookup-location (first ls) sto)]
    [else (find m (rest ms) (rest ls) sto)]))

(define (lookup-location [loc : Location] [sto : Store]) : Value
  (cond
    [(empty? sto) (error 'interp "no such field")]
    [(= loc (cell-location (first sto))) (cell-val (first sto))]
    [else (lookup-location loc (rest sto))]))


(define (update-store [c : Storage] [sto : Store] [before : Store] [original : Store]) : Store
  (cond
    [(empty? sto) (override-store c original)]
    [(eq? (cell-location (first sto)) (cell-location c)) (append (override-store c before) (rest sto))]
    [else (update-store c (rest sto) (override-store (first sto) before) original)]))

(define (sub-store [exps : (listof ExprC)] [env : Env] [sto : Store]) : Store
  (cond
    [(empty? exps) sto]
    [else (sub-store (rest exps) env (v*s-s (interp (first exps) env sto)))]))



(module+ test
  (test (update-store (cell 3 (numV 1)) mt-store mt-store mt-store)
        (list (cell 3 (numV 1))))
  (test (update-store (cell 3 (numV 1)) (list (cell 2 (numV 5))) mt-store (list (cell 2 (numV 5))))
        (list (cell 3 (numV 1)) (cell 2 (numV 5))))
  (test (interp (parse '2) mt-env mt-store)
        (v*s (numV 2) 
             mt-store))
  (test/exn (interp (parse `x) mt-env mt-store)
            "free variable")
  (test (interp (parse `x) 
                (extend-env (bind 'x (numV 9)) mt-env)
                mt-store)
        (v*s (numV 9)
             mt-store))
  (test (interp (parse '{+ 2 1}) mt-env mt-store)
        (v*s (numV 3)
             mt-store))
  (test (interp (parse '{* 2 1}) mt-env mt-store)
        (v*s (numV 2)
             mt-store))
  (test (interp (parse '{+ {* 2 3} {+ 5 8}})
                mt-env
                mt-store)
        (v*s (numV 19)
             mt-store))
  (test (interp (parse '{lambda {x} {+ x x}})
                mt-env
                mt-store)
        (v*s (closV 'x (plusC (idC 'x) (idC 'x)) mt-env)
             mt-store))
  (test (interp (parse '{let {[x 5]}
                          {+ x x}})
                mt-env
                mt-store)
        (v*s (numV 10)
             mt-store))
  (test (interp (parse '{let {[x 5]}
                          {let {[x {+ 1 x}]}
                            {+ x x}}})
                mt-env
                mt-store)
        (v*s (numV 12)
             mt-store))
  (test (interp (parse '{let {[x 5]}
                          {let {[y 6]}
                            x}})
                mt-env
                mt-store)
        (v*s (numV 5)
             mt-store))
  (test (interp (parse '{{lambda {x} {+ x x}} 8})
                mt-env
                mt-store)
        (v*s (numV 16)
             mt-store))
  (test (interp (parse '{box 5}) 
                mt-env
                mt-store)
        (v*s (boxV 1)
             (override-store (cell 1 (numV 5))
                             mt-store)))
  (test (interp (parse '{unbox {box 5}})
                mt-env
                mt-store)
        (v*s (numV 5)
             (override-store (cell 1 (numV 5))
                             mt-store)))

  (test (interp (parse '{begin 1 2})
                mt-env
                mt-store)
        (v*s (numV 2)
             mt-store))

  (test (interp (parse '{let {[b {box 1}]}
                          {begin
                           {set-box! b 2}
                           {unbox b}}})
                mt-env
                mt-store)
        (v*s (numV 2)
             (override-store (cell 1 (numV 2))
                             mt-store)))

  (test (interp (parse '{let {[b {box 1}]}
                          {begin
                           {set-box! b {+ 2 {unbox b}}}
                           {set-box! b {+ 3 {unbox b}}}
                           {set-box! b {+ 4 {unbox b}}}
                           {unbox b}}})
                mt-env
                mt-store)
        (v*s (numV 10)
             (override-store (cell 1 (numV 10)) 
                             mt-store)))


  (test (interp (parse '(begin (+ 1 1))) mt-env mt-store)
        (v*s (numV 2) mt-store))
  
  ;; record and get tests
  (test (interp (parse '{record {a 10} {b {+ 1 2}}}) mt-env mt-store)
        (v*s (recV (list 'a 'b) (list 1 2)) (override-store (cell 2 (numV 3)) (override-store (cell 1 (numV 10)) mt-store))))
  
  (test (interp-expr (parse '{+ 1 4}))
        '5)
  (test (interp-expr (parse '{record {a 10} {b {+ 1 2}}}))
        `record)
  (test (interp-expr (parse '(lambda (x) (+ x 9))))
        `function)

   (test (interp-expr (parse '{get {record {a 10} {b {+ 1 0}}} b}))
        '1)
  (test/exn (interp-expr (parse '{get {record {a 10}} b}))
            "no such field")
  (test (interp-expr (parse '{get {record {r {record {z 0}}}} r}))
        `record)
  (test (interp-expr (parse '(box 1)))
        `box)

  (test (interp (parse '{record {x 5}}) mt-env mt-store)
        (v*s (recV (list 'x) (list 1)) (list (cell 1 (numV 5)))))

  (test (interp (parse '{begin {box 9} {record {x 5}}}) mt-env mt-store)
        (v*s (recV (list 'x) (list 2)) (list (cell 2 (numV 5)) (cell 1 (numV 9))))) 

  
  (test (interp (parse '{get {record {z 0}} z}) mt-env mt-store)
        (v*s (numV 0) (list (cell 1 (numV 0)))))

  (test (interp (parse '{record {r {record {z 0}}}}) mt-env mt-store) 
        (v*s (recV (list 'r) (list 2)) (list (cell 2 (recV (list 'z) (list 1))) (cell 1 (numV 0))))) 
  
  (test (interp (parse '{get {get {record {r {record {z 0}}}} r} z}) mt-env mt-store)
        (v*s (numV 0) (list (cell 2 (recV (list 'z) (list 1))) (cell 1 (numV 0))))) 
  
  (test (interp-expr (parse '{get {get {record {r {record {z 0}}}} r} z})) 
        '0)

  ;; mutate tests
  (test (interp-expr (parse '{let {[r {record {x 1}}]}
                               {get r x}}))
        '1)

  (test (interp (parse '{let {[r {record {x 1}}]}
                                 {set r x 5}})
                mt-env mt-store)
        (v*s (numV 5) (list (cell 1 (numV 5)))))
  
  (test (interp-expr (parse '{let {[r {record {x 1}}]}
                               {begin
                                 {set r x 5}
                                 {get r x}}}))
        '5) 

  (test (interp-expr (parse '{let {[r {record {x 1}}]}
                               {let {[get-r {lambda {d} r}]}
                                 {begin
                                   {set {get-r 0} x 6}
                                   {get {get-r 0} x}}}}))
        '6)

  (test (interp-expr (parse '{let {[g {lambda {r} {get r a}}]}
                               {let {[s {lambda {r} {lambda {v} {set r b v}}}]}
                                 {let {[r1 {record {a 0} {b 2}}]}
                                   {let {[r2 {record {a 3} {b 4}}]}
                                     {+ {get r1 b}
                                        {begin
                                          {{s r1} {g r2}}
                                          {+ {begin
                                               {{s r2} {g r1}}
                                               {get r1 b}}
                                             {get r2 b}}}}}}}}))
        '5)

  (test (interp (parse '{let {[g {lambda {r} {get r a}}]}
                               {let {[s {lambda {r} {lambda {v} {set r b v}}}]}
                                 {let {[r1 {record {a 0} {b 2}}]}
                                   {let {[r2 {record {a 3} {b 4}}]}
                                     {+ {get r1 b}
                                        {begin
                                          {{s r1} {g r2}}
                                          {+ {begin
                                               {{s r2} {g r1}}
                                               {get r1 b}}
                                             {get r2 b}}}}}}}}) mt-env mt-store)
        (v*s (numV 5) (list (cell 4 (numV 0)) (cell 3 (numV 3)) (cell 2 (numV 3)) (cell 1 (numV 0)))))

  (test (interp (parse '{let {[r1 {record {a 0} {b 2}}]}
                                   {let {[r2 {record {a 3} {b 4}}]}
                          (begin
                            (set r1 b 5)
                            (get r1 b))}})
                mt-env
                mt-store)
        (v*s (numV 5) (list (cell 4 (numV 4)) (cell 3 (numV 3)) (cell 2 (numV 5)) (cell 1 (numV 0)))))

  (test (interp (parse '(record {x 10} {y 20} {z 30} {a 40}))
                mt-env mt-store)
        (v*s (recV (list 'x 'y 'z 'a) (list 1 2 3 4)) (list (cell 4 (numV 40)) (cell 3 (numV 30)) (cell 2 (numV 20)) (cell 1 (numV 10)))))

  
  
  ;; exception tests
  (test/exn (interp (parse '{1 2}) mt-env mt-store)
            "not a function")
  (test/exn (interp (parse '{+ 1 {lambda {x} x}}) mt-env mt-store)
            "not a number")
  (test/exn (interp (parse '{let {[bad {lambda {x} {+ x y}}]}
                              {let {[y 5]}
                                {bad 2}}})
                    mt-env
                    mt-store)
            "free variable")
  (test/exn (interp (parse '{unbox 1}) mt-env mt-store)
            "not a box")
  (test/exn (interp (parse '{set-box! 1 2}) mt-env mt-store)
            "not a box")
  (test/exn (interp (parse '{get (+ 1 1) a}) mt-env mt-store)
            "not a record")
  (test/exn (interp (parse '{set (box 2) a 1}) mt-env mt-store)
            "not a record")
  (test/exn (find-location empty empty 'n)
            "field not found")
  (test/exn (change-store 1 (numV 1) mt-store mt-store)
            "field not found")
  (test/exn (lookup-location 1 mt-store)
            "no such field")
  )

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
(define (lookup [n : symbol] [env : Env]) : Value
  (cond
   [(empty? env) (error 'lookup "free variable")]
   [else (cond
          [(symbol=? n (bind-name (first env)))
           (bind-val (first env))]
          [else (lookup n (rest env))])]))

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
  
;; store operations ----------------------------------------

(define (new-loc [sto : Store]) : Location
  (+ 1 (max-address sto)))

(define (max-address [sto : Store]) : Location
  (cond
   [(empty? sto) 0]
   [else (max (cell-location (first sto))
              (max-address (rest sto)))]))

(define (fetch [l : Location] [sto : Store]) : Value
  (cond
   [(empty? sto) (error 'interp "unallocated location")]
   [else (if (equal? l (cell-location (first sto)))
             (cell-val (first sto))
             (fetch l (rest sto)))]))

(module+ test
  (test (max-address mt-store)
        0)
  (test (max-address (override-store (cell 2 (numV 9))
                                     mt-store))
        2)
  
  (test (fetch 2 (override-store (cell 2 (numV 9))
                                 mt-store))
        (numV 9))
  (test (fetch 2 (override-store (cell 2 (numV 10))
                                 (override-store (cell 2 (numV 9))
                                                 mt-store)))
        (numV 10))
  (test (fetch 3 (override-store (cell 2 (numV 10))
                                 (override-store (cell 3 (numV 9)) 
                                                 mt-store)))
        (numV 9))
  (test/exn (fetch 2 mt-store)
            "unallocated location"))




