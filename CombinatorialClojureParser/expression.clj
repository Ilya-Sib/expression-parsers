(defn abstract-expr [op]
  (fn [& args]
    (fn [vals]
      (apply op (map #(% vals) args)))))

(def add (abstract-expr +))
(def subtract (abstract-expr -))
(def multiply (abstract-expr *))
(def divide (abstract-expr (fn
                             ([den] (/ 1 (double den)))
                             ([num & den] (/ (double num) (reduce * den))))))
(def sum add)
(def avg (abstract-expr #(/ (apply + %&) (count %&))))
(def negate subtract)

(def constant constantly)
(defn variable [name]
  (fn [vals]
    (vals name)))

(def ops {'+      add
          '-      subtract
          '*      multiply
          '/      divide
          'negate negate
          'sum    sum
          'avg    avg})

(defn parser-factory [obj-map const _var]
  (fn expression-parser [expression]
    (cond
      (list? expression) (let [[op & args] expression]
                           (apply (obj-map op) (map expression-parser args)))
      (number? expression) (const expression)
      (symbol? expression) (_var (str expression)))))

(defn parseFunction [expression]
  ((parser-factory ops constant variable) (read-string expression)))

(load-file "proto.clj")

(def data (field :data))
(def toString (method :toString))
(def toStringInfix (method :toStringInfix))
(def evaluate (method :evaluate))
(def diff (method :diff))

(defn constructorFactory [toString toStringInfix evaluate diff]
  (constructor (fn [this & data]
                 (assoc this
                   :data data))
               {:toString      toString
                :toStringInfix toStringInfix
                :evaluate      evaluate
                :diff          diff}))

(def ZERO)
(def Constant (constructorFactory
                (fn [this] (format "%.1f" (double (first (data this)))))
                (fn [this] (format "%.1f" (double (first (data this)))))
                (fn [this _] (first (data this)))
                (fn [_ __] ZERO)))
(def ZERO (Constant 0))
(def ONE (Constant 1))


(def Variable (constructorFactory
                (fn [this] (first (data this)))
                (fn [this] (first (data this)))
                (fn [this values] (values (clojure.string/lower-case (first (first (data this))))))
                (fn [this diff-name] (cond
                                       (= (clojure.string/lower-case (first (first (data this)))) diff-name) ONE
                                       :else ZERO))))

(defn operationFactory [operationSymbol computation diffRule]
  (constructorFactory
    (fn [this]
      (str "(" operationSymbol " "
           (clojure.string/join " " (map toString (data this))) ")"))
    (fn [this] (cond
                 (== (count (data this)) 1) (str operationSymbol "(" (toStringInfix (first (data this))) ")")
                 :else (str "(" (clojure.string/join (str " " operationSymbol " ") (map toStringInfix (data this))) ")")))
    (fn [this values] (apply computation
                             (map #(evaluate % values) (data this))))
    (fn [this diff-name] (diffRule (data this)
                                   (map #(diff % diff-name) (data this))))))

(defn calculator [op]
  (fn [args]
    (apply op args)))

(def Add (operationFactory "+"
                           +
                           #(apply Add %2)))
(def Subtract (operationFactory "-"
                                -
                                #(apply Subtract %2)))
(def Negate (operationFactory "negate"
                              -
                              #(apply Negate %2)))
(def Multiply)
(def Divide)

(defn diffMultiply [args args']
  (nth (reduce
         (fn [[u u'] [v v']]
           (list (Multiply u v)
                 (Add (Multiply u' v) (Multiply u v'))))
         (map list args args')) 1))

(defn diffDivide [[u & v] [u' & v']]
  (cond
    (empty? v) (Negate (Divide u' (Multiply u u)))
    :else (Divide (Subtract
                    (apply Multiply u' v)
                    (Multiply u (diffMultiply v v')))
                  (Multiply (apply Multiply v) (apply Multiply v)))))

(def Multiply (operationFactory "*"
                                *
                                diffMultiply))
(def Divide (operationFactory "/"
                              (fn
                                ([den] (/ 1 (double den)))
                                ([num & den] (/ (double num) (reduce * den))))
                              diffDivide))

(def Sum (operationFactory "sum"
                           +
                           #(apply Sum %2)))

(def Avg (operationFactory "avg"
                           #(/ (apply + %&) (count %&))
                           #(Divide (apply Sum %2) (Constant (count %2)))))

(def IPow (operationFactory "**"
                            #(Math/pow %1 %2)
                            constantly))

(def ILog (operationFactory "//"
                            #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1)))
                            constantly))

(def Sin (operationFactory "sin"
                           #(Math/sin %)
                           constantly))

(def Cos (operationFactory "cos"
                           #(Math/cos %)
                           constantly))

(def object-ops {'+            Add
                 '-            Subtract
                 '*            Multiply
                 '/            Divide
                 'negate       Negate
                 'sum          Sum
                 'avg          Avg
                 '**           IPow
                 (symbol "//") ILog
                 'sin          Sin
                 'cos          Cos})

(defn parseObject [expression]
  ((parser-factory object-ops Constant Variable) (read-string expression)))

(load-file "parser.clj")

(def *digits (+char "0123456789"))
(def *spaces (+char " \t\n\r"))
(def *chars (apply str (map char (filter #(Character/isLetter %) (range 32 128)))))
(def *ws (+ignore (+star *spaces)))
(def *letter (+char *chars))
; (letter | digits)*
(def *identifier (+str (+seqf cons *letter (+star (+or *letter *digits)))))
; (-? digits* .? digits*?)
(def *number (+map read-string
                   (+str (+seqf
                           concat
                           (+seq (+opt (+char "-")))
                           (+plus *digits)
                           (+seq (+opt (+char ".")))
                           (+opt (+plus *digits))))))
(defn *operation [op-symbol]
  (+seqf (constantly (object-ops op-symbol))
         (apply +seq (map (comp +char str) (str op-symbol)))))

(defn *fold-factory [f-args f-reduce]
  (fn [op-seq]
    (let [[head & tail] (f-args op-seq)]
      (reduce f-reduce head (partition 2 tail)))))

; next-prior ((op-symbols...) next-prior)*
(defn *op-seq [next-prior op-symbols]
  (+seqf (partial apply concat)
         *ws (+seq next-prior) *ws
         (+star (+seq (apply +or (map *operation op-symbols))
                      next-prior)) *ws))

(defn *left-op-parse-factory [next-prior & op-symbols]
  (+map
    (*fold-factory identity (fn [a [op b]] (op a b)))
    (*op-seq next-prior op-symbols)))

(defn *right-op-parse-factory [next-prior & op-symbols]
  (+map
    (*fold-factory reverse (fn [a [op b]] (op b a)))
    (*op-seq next-prior op-symbols)))

; number
(def *constant (+map Constant *number))
; identifier
(def *variable (+map Variable *identifier))
(declare *factor)
; :NOTE: Как добавлять функции? :OUTDATED:
; 'op-symbol' factor
(defn *unary-parse-factory [op-symbol]
  (+map (fn [args]
          (let [[op arg] args]
            (op arg)))
        ; :NOTE: var :OUTDATED:
        (+seq (*operation op-symbol) *ws (delay *factor))))

(declare *add-sub)
; (unary-ops...) | constant | variable | '(' add-sub ')'
(def *factor
  (+seqn 0 *ws
         (+or
           (apply +or (map *unary-parse-factory ['negate 'sin 'cos]))
           *constant
           *variable
           (+seqn 1 (+char "(") *ws (delay *add-sub) *ws (+char ")"))) *ws))

; factor (('**' | '//') factor)*
(def *pow-log (*right-op-parse-factory *factor '** (symbol "//")))
; pow-log (('*' | '\') pow-log)*
(def *mul-div (*left-op-parse-factory *pow-log '* '/))
; mul-div (('+' | '-') mul-div)*
(def *add-sub (*left-op-parse-factory *mul-div '+ '-))

; add-sub* EOF
(def parseObjectInfix (+parser *add-sub))
