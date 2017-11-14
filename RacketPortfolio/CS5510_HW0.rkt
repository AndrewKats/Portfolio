#lang plai-typed

;; Andrew Katsanevas
;; 8/26/2016
;; CS 5510

;; Part 1: 3rd-power
;; Takes a number and returns that number raised to the 3rd power.
(define (3rd-power [x : number])
  (* x (* x x)))

;; Tests for 3rd-power
(module+ test
  (print-only-errors true)
  (test (3rd-power 17) 4913)
  (test (3rd-power 0) 0))

;; Helper function for building up to 42nd-power.
(define (6th-power [x : number])
  (* (3rd-power x) (3rd-power x)))

;; Helper function for building up to 42nd-power.
(define (12th-power [x : number])
  (* (6th-power x) (6th-power x)))


;; Part 2: 42nd-power
;; Takes a number and returns that number raised to the 42nd power.
(define (42nd-power [x : number])
  (* (6th-power x) (* (12th-power x) (* (12th-power x) (12th-power x)))))

;; Tests for 42nd-power
(module+ test
  (test (42nd-power 17) 4773695331839566234818968439734627784374274207965089)
  (test (42nd-power 0) 0))


;; Part 3: plural
;; Returns the plural of a string. If it ends in "y", replaces it with "ies". Otherwise appends "s".
(define (plural [s : string])
  (cond
    [(= 0 (string-length s)) "s"]
    [(char=? (string-ref s (- (string-length s) 1)) #\y) (string-append (substring s 0 (- (string-length s) 1)) "ies")]
    [else (string-append s "s")]))

;; Tests for plural
(module+ test
  (test (plural "baby") "babies")
  (test (plural "fish") "fishs")
  (test (plural "") "s"))

;; Light type which can be bulb or candle.
(define-type Light
    [bulb (watts : number)
          (technology : symbol)]
    [candle (inches : number)])

;; Part 4: energy-usage
;; Returns the killowatthours of a light in 24 hours.
(define (energy-usage [l : Light])
  (type-case Light l
    [bulb (w t) (* w 0.024)]
    [candle (i) 0]))

;; Tests for energy-usage
(module+ test
  (test (energy-usage (bulb 100.0 'halogen)) 2.4)
  (test (energy-usage (bulb 0 'halogen)) 0)
  (test (energy-usage (candle 10.0)) 0))


;; Part 5: use-for-one-hour
;; Returns the change in a light if used for an hour.
(define (use-for-one-hour [l : Light])
  (type-case Light l
    [bulb (w t) l]
    [candle (i)
            (cond
              [(< (- i 1) 0) (candle 0)]
              [else (candle (- i 1))])]))

;; Tests for use-for-one-hour
(module+ test
  (test (use-for-one-hour (bulb 100.0 'halogen)) (bulb 100.0 'halogen))
  (test (use-for-one-hour (bulb 0 'fluorescent)) (bulb 0 'fluorescent))
  (test (use-for-one-hour (candle 10.0)) (candle 9.0))
  (test (use-for-one-hour (candle 0)) (candle 0))
  (test (use-for-one-hour (candle 0.5)) (candle 0)))