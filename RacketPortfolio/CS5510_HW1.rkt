#lang plai-typed

;; Andrew Katsanevas
;; 8/31/2016
;; CS 5510

(define-type Tree
    [leaf (val : number)]
    [node (val : number)
          (left : Tree)
          (right : Tree)])

;; Part 1 - Sum
(define (sum [t : Tree]) : number
  (type-case Tree t
    [leaf (v) v]
    [node (v l r) (+ v (+ (sum l) (sum r)))]))


;; Part 2 - Negate
(define (negate [t : Tree]) : Tree
  (type-case Tree t
    [leaf (v) (leaf (- 0 v))]
    [node (v l r) (node (- 0 v) (negate l) (negate r))]))


;; Part 3 - Contains?
(define (contains? [t : Tree] [n : number]) : boolean
  (type-case Tree t
    [leaf (v) (= n v)]
    [node (v l r) (or (= n v) (or (contains? l n) (contains? r n)))]))


;; Part 4 - Big Leaves?
(define (bigger-leaves? [t : Tree] [n : number]) : boolean
    (type-case Tree t
    [leaf (v) (> v n)]
    [node (v l r) (and (bigger-leaves? l (+ v n)) (bigger-leaves? r (+ v n)))]))

(define (big-leaves? [t : Tree]) : boolean
  (bigger-leaves? t 0))


;; Part 5 - Sorted?
(define (sorted-helper [t : Tree] [min : number] [max : number]) : boolean
  (type-case Tree t
    [leaf (v) (cond
                [(< v min) #f]
                [(> v max) #f]
                [else #t])]
    [node (v l r) (cond
                    [(< v min) #f]
                    [(> v max) #f]
                    [else (and (sorted-helper l min (- v 1)) (sorted-helper r v max))])]))

(define (sorted? [t : Tree]) : boolean
  (sorted-helper t -999999999 999999999))

;; TESTS
(module+ test
  (print-only-errors true)
  
  ;; Sum tests
  (test (sum (node 5 (leaf 6) (leaf 7))) 18)
  (test (sum (node 4 (node 2 (leaf 1) (leaf 3)) (leaf 5))) 15)
  (test (sum (leaf 5)) 5)

  ;; Negate tests
  (test (negate (node 5 (leaf 6) (leaf 7))) (node -5 (leaf -6) (leaf -7)))
  (test (negate (leaf 5)) (leaf -5))
  (test (negate (leaf 0)) (leaf 0))

  ;; Contains? tests
  (test (contains? (node 5 (leaf 6) (leaf 7)) 6) #t)
  (test (contains? (node 5 (leaf 6) (leaf 7)) 8) #f)
  (test (contains? (leaf 6) 6) #t)
  (test (contains? (leaf 6) 8) #f)

  ;; Big Leaves? tests
  (test (big-leaves? (node 5 (leaf 6) (leaf 7))) #t)
  (test (big-leaves? (node 5 (node 2 (leaf 8) (leaf 6)) (leaf 7))) #f)
  (test (big-leaves? (leaf 5)) #t)

  ;; Sorted tests
  (test (sorted? (node 5 (leaf 4) (leaf 7))) #t)
  (test (sorted? (node 5 (leaf 2) (leaf 8))) #t)
  (test (sorted? (node 2 (leaf 1) (leaf 2))) #t)
  (test (sorted? (node 2 (leaf 1) (node 3 (leaf 2) (leaf 3)))) #t)
  (test (sorted? (node 4 (node 2 (leaf 1) (leaf 3)) (leaf 5))) #t)
  (test (sorted? (node 5 (leaf 6) (leaf 7))) #f)
  (test (sorted? (node 5 (leaf 8) (leaf 7))) #f)
  (test (sorted? (node 5 (node 2 (leaf 8) (leaf 6)) (leaf 7))) #f)
  (test (sorted? (node 3 (node 2 (leaf 1) (leaf 4)) (leaf 5))) #f)
  (test (sorted? (node 2 (leaf 1) (node 3 (leaf 1) (leaf 3)))) #f)
  (test (sorted? (node 5 (node 6 (leaf 4) (leaf 7)) (leaf 7))) #f)
  (test (sorted? (node 5 (node 3 (leaf 2) (leaf 3)) (node 4 (leaf 4) (leaf 8)))) #f)
  (test (sorted? (leaf 5)) #t))
  