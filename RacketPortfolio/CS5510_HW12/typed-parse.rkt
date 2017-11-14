#lang plai-typed
(require plai-typed/s-exp-match
         "class.rkt"
         "inherit.rkt"
         "typed-class.rkt"
         "inherit-parse.rkt")

(module+ test
  (print-only-errors true))

;; ----------------------------------------

(define (parse-t-class [s : s-expression]) : ClassT
  (cond
   [(s-exp-match? `{class SYMBOL extends SYMBOL {ANY ...} ANY ...} s)
    (classT (s-exp->symbol (second (s-exp->list s)))
            (s-exp->symbol (fourth (s-exp->list s)))
            (map parse-t-field
                 (s-exp->list (fourth (rest (s-exp->list s)))))
            (map parse-t-method 
                 (rest (rest (rest (rest (rest (s-exp->list s))))))))]
   [else (error 'parse-t-class "invalid input")]))

(define (parse-t-field [s : s-expression]) : FieldT
  (cond
   [(s-exp-match? `[SYMBOL : ANY] s)
    (fieldT (s-exp->symbol (first (s-exp->list s)))
            (parse-type (third (s-exp->list s))))]
   [else (error 'parse-t-field "invalid input")]))

(define (parse-t-method [s : s-expression]) : MethodT
  (cond
   [(s-exp-match? `{SYMBOL : ANY -> ANY ANY} s)
    (methodT (s-exp->symbol (first (s-exp->list s)))
             (parse-type (third (s-exp->list s)))
             (parse-type (fourth (rest (s-exp->list s))))
             (parse (fourth (rest (rest (s-exp->list s))))))]
   [else (error 'parse-t-method "invalid input")]))

(define (parse-type [s : s-expression]) : Type
  (cond
   [(s-exp-match? `num s)
    (numT)]
   [(s-exp-match? `SYMBOL s)
    (objT (s-exp->symbol s))]
   [else (error 'parse-type "invalid input")]))

(module+ test
  (test (parse-type `num)
        (numT))
  (test (parse-type `object)
        (objT 'object))
  (test/exn (parse-type `{})
            "invalid input")
  
  (test (parse-t-field `[x : num])
        (fieldT 'x (numT)))
  (test/exn (parse-t-field '{x 1})
            "invalid input")

  (test (parse-t-method `{m : num -> object this})
        (methodT 'm (numT) (objT 'object) (thisI)))
  (test/exn (parse-t-method `{m 1})
            "invalid input")
  
  (test (parse-t-class '{class posn3D extends posn
                               {[x : num] [y : num]}
                               {m1 : num -> num arg}
                               {m2 : num -> object this}})
        (classT 'posn3D 'posn
                (list (fieldT 'x (numT))
                      (fieldT 'y (numT)))
                (list (methodT 'm1 (numT) (numT) (argI))
                      (methodT 'm2 (numT) (objT 'object) (thisI)))))
  (test/exn (parse-t-class '{class})
            "invalid input"))

;; ----------------------------------------

(define (interp-t-prog [classes : (listof s-expression)] [a : s-expression]) : s-expression
  (let ([v (interp-t (parse a)
                     (map parse-t-class classes))])
    (type-case Value v
      [numV (n) (number->s-exp n)]
      [objV (class-name field-vals) `object]
      [nullV () `null])))

(module+ test
  (test (interp-t-prog
         (list
          '{class empty extends object
                  {}})
         '{new empty})
        `object)

 (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class posn3D extends posn
                 {[z : num]}
                 {mdist : num -> num
                        {+ {get this z} 
                           {super mdist arg}}}})
        
        '{send {new posn3D 5 3 1} addDist {new posn 2 7}})
       '18)

  ;; ==================================================================================
  ;; NEW INTERP TESTS
  ;; ==================================================================================

  ;; this/arg
  (test (interp-t-prog
         empty
         `this)
        '-1)
  
  (test (interp-t-prog
         empty
         `arg)
        '-1)

  ;; instanceof
  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class posn3D extends posn
                 {[z : num]}
                 {mdist : num -> num
                        {+ {get this z} 
                           {super mdist arg}}}})
        
        '{instanceof {new posn3D 5 3 1} posn})
       '0)

  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class posn3D extends posn
                 {[z : num]}
                 {mdist : num -> num
                        {+ {get this z} 
                           {super mdist arg}}}})
        
        '{instanceof {new posn 5 3} posn})
       '0)

  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class posn3D extends posn
                 {[z : num]}
                 {mdist : num -> num
                        {+ {get this z} 
                           {super mdist arg}}}})
        
        '{instanceof {new posn 5 3} posn3D})
       '1)

  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class posn3D extends posn
                 {[z : num]}
                 {mdist : num -> num
                        {+ {get this z} 
                           {super mdist arg}}}})
        
        '{instanceof {new posn 5 3} object})
       '0)
  
  (test/exn (interp-t-prog 
        empty       
        '{instanceof 1 object})
       "not an object")
  
  ;; if0
  (test (interp-t-prog
         empty
         '(if0 0 1 2))
        '1)

  (test (interp-t-prog
         empty
         '(if0 1 1 2))
        '2)

  (test (interp-t-prog
         empty
         '(if0 0 1 null))
        '1)
  
  (test (interp-t-prog
         empty
         '(if0 1 1 null))
        `null)

  (test/exn (interp-t-prog
         empty
         '(if0 null 1 2))
        "not a number")

  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class testclass extends object
                 {[x : num]}
                 {trynull2 : num -> num
                           42}})
        '(if0 0 (new posn 1 2) (new testclass 0)))
       `object)

  ;; cast
  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class posn3D extends posn
                 {[z : num]}
                 {mdist : num -> num
                        {+ {get this z} 
                           {super mdist arg}}}})
        
        '{cast object {new posn 5 3}})
       `object)

  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class posn3D extends posn
                 {[z : num]}
                 {mdist : num -> num
                        {+ {get this z} 
                           {super mdist arg}}}})
        
        '{cast posn {new posn3D 5 3 4}})
       `object)

  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class posn3D extends posn
                 {[z : num]}
                 {mdist : num -> num
                        {+ {get this z} 
                           {super mdist arg}}}})
        
        '{cast posn {new posn 5 3}})
       `object)

  (test/exn (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class posn3D extends posn
                 {[z : num]}
                 {mdist : num -> num
                        {+ {get this z} 
                           {super mdist arg}}}})
        
        '{cast posn3D {new posn 5 3}})
       "not a subclass")
  
  (test/exn (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class testclass extends object
                 {[x : num]}
                 {trynull2 : num -> num
                           42}})
        '{cast testclass {new posn 5 3}})
       "not a subclass")
  
  ;; null
  (test (interp-t-prog
         empty
         `null)
        `null)

  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class nulltest2 extends object
                 {[x : posn]}
                 {trynull2 : posn -> posn
                           {get this x}}})
        '(send (new nulltest2 (new posn 2 7)) trynull2 null))
       `object)

  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class nulltest2 extends object
                 {[x : posn]}
                 {trynull2 : posn -> posn
                           arg}})
        '(send (new nulltest2 (new posn 2 7)) trynull2 null))
       `null)

  (test (interp-t-prog 
        (list
         '{class posn extends object
                 {[x : num]
                  [y : num]}
                 {mdist : num -> num
                        {+ {get this x} {get this y}}}
                 {addDist : posn -> num
                          {+ {send arg mdist 0}
                             {send this mdist 0}}}}
         
         '{class nulltest2 extends object
                 {[x : posn]}
                 {trynull2 : posn -> posn
                           {get this x}}})
        '(send (new nulltest2 null) trynull2 null))
       `null)

  (test/exn (interp-t-prog 
        empty
        '(+ null 1))
       "not a number")
  

  
  )