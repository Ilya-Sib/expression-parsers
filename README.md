# Парсеры
## [Java](https://github.com/Ilya-Sib/expression-parsers/tree/main/JavaGenericParser/src/expression)
### Задание

Разработайте классы `Const, Variable, Add, Subtract, Multiply, Divide` для вычисления выражений с одной переменной в типе int.
Классы должны позволять составлять выражения вида

```
  new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
).evaluate(5)
```
            
При вычислении такого выражения вместо каждой переменной подставляется значение, переданное в качестве параметра методу evaluate (на данном этапе имена переменных игнорируются). Таким образом, результатом вычисления приведенного примера должно стать число 7.
Метод `toString` должен выдавать запись выражения в полноскобочной форме. 
Например

```  
  new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
).toString()
```
            
должен выдавать ((2 * x) - 3).

Реализуйте метод `equals`, проверяющий, что два выражения совпадают. 
Например,
``` 
  new Multiply(new Const(2), new Variable("x"))
    .equals(new Multiply(new Const(2), new Variable("x")))
```
            
должно выдавать true, а
```
  new Multiply(new Const(2), new Variable("x"))
    .equals(new Multiply(new Variable("x"), new Const(2)))
```

должно выдавать false.
Для тестирования программы должен быть создан класс Main, который вычисляет значение выражения x2−2x+1, для x, заданного в командной строке.
При выполнении задания следует обратить внимание на:
Выделение общего интерфейса создаваемых классов.
Выделение абстрактного базового класса для бинарных операций.

Доработайте предыдущее домашнее задание, так что бы выражение строилось по записи вида
x * (x - 2)*x + 1
В записи выражения могут встречаться: умножение *, деление /, сложение +, вычитание -, унарный минус -, целочисленные константы (в десятичной системе счисления, которые помещаются в 32-битный знаковый целочисленный тип), круглые скобки, переменные (x) и произвольное число пробельных символов в любом месте (но не внутри констант).
Приоритет операторов, начиная с наивысшего
унарный минус;
умножение и деление;
сложение и вычитание.
Разбор выражений рекомендуется производить методом рекурсивного спуска. Алгоритм должен работать за линейное время.

Добавьте в программу вычисляющую выражения обработку ошибок, в том числе:
ошибки разбора выражений;
ошибки вычисления выражений.
Для выражения 1000000*x*x*x*x*x/(x-1) вывод программы должен иметь следующий вид:
```
x       f
0       0
1       division by zero
2       32000000
3       121500000
4       341333333
5       overflow
6       overflow
7       overflow
8       overflow
9       overflow
10      overflow
```
            
Результат division by zero (overflow) означает, что в процессе вычисления произошло деление на ноль (переполнение).
При выполнении задания следует обратить внимание на дизайн и обработку исключений.
Человеко-читаемые сообщения об ошибках должны выводится на консоль.
Программа не должна «вылетать» с исключениями (как стандартными, так и добавленными).

Добавьте в программу разбирающую и вычисляющую выражения трех переменных поддержку вычисления в различных типах.

Создайте класс `expression.generic.GenericTabulator`, реализующий интерфейс `expression.generic.Tabulator`:

```public interface Tabulator {
    Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws Exception;
}
```

Аргументы

mode — режим работы
Режим	Тип
i	int (с детекцией переполнений)
d	double
bi	BigInteger
expression — вычисляемое выражение;
x1, x2; y1, y2; z1, z2 — диапазоны изменения переменны (включительно).
Возвращаемое значение — таблица значений функции, где R[i][j][k] соответствует x = x1 + i, y = y1 + j, z = z1 + k. Если вычисление завершилось ошибкой, в соответствующей ячейке должен быть null.

Доработайте интерфейс командной строки:
Первым аргументом командной строки программа должна принимать указание на тип, в котором будут производится вычисления:
Опция	Тип
-i	int (с детекцией переполнений)
-d	double
-bi	BigInteger
Вторым аргументом командной строки программа должна принимать выражение для вычисления.
Программа должна выводить результаты вычисления для всех целочисленных значений переменных из диапазона −2..2.
Реализация не должна содержать непроверяемых преобразований типов.
Реализация не должна использовать аннотацию @SuppressWarnings.
При выполнении задания следует обратить внимание на простоту добавления новых типов и операциий.

## [JavaStript](https://github.com/Ilya-Sib/expression-parsers/tree/main/JSPrefixSuffixParser)
# Задание

Разработайте классы `Const, Variable, Add, Subtract, Multiply, Divide, Negate` для представления выражений с одной переменной.
Пример описания выражения 2x-3:
```
let expr = new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
);

println(expr.evaluate(5));
```

При вычислении такого выражения вместо каждой переменной подставляется её значение, переданное в качестве аргумента метода evaluate. Таким образом, результатом вычисления приведенного примера должно стать число 7.
Метод `toString()` должен выдавать запись выражения в обратной польской записи. Например, `expr.toString()` должен выдавать «2 x * 3 -».
Сложный вариант.
Метод `diff("x")` должен возвращать выражение, представляющее производную исходного выражения по переменной x. Например, `expr.diff("x")` должен возвращать выражение, эквивалентное `new Const(2) (выражения new Subtract(new Const(2), new Const(0))` и
```
new Subtract(
    new Add(
        new Multiply(new Const(0), new Variable("x")),
        new Multiply(new Const(2), new Const(1))
    )
    new Const(0)
)
```
                 
так же будут считаться правильным ответом).
Функция parse должна выдавать разобранное объектное выражение.

Добавьте в предыдущее домашнее задание функцию parsePrefix(string), разбирающую выражения, задаваемые записью вида «(- (* 2 x) 3)». Если разбираемое выражение некорректно, метод parsePrefix должен бросать человеко-читаемое сообщение об ошибке.
Добавьте в предыдущее домашнее задание метод prefix(), выдающий выражение в формате, ожидаемом функцией parsePrefix.
При выполнение задания следует обратить внимание на:
Применение инкапсуляции.
Выделение общего кода для операций.
Минимизацию необходимой памяти.
Обработку ошибок.

## [Clojure](https://github.com/Ilya-Sib/expression-parsers/tree/main/CombinatorialClojureParser)
# Задание

Разработайте конструкторы `Constant, Variable, Add, Subtract, Multiply и Divide` для представления выражений с одной переменной.
Пример описания выражения 2x-3:
```
(def expr
  (Subtract
    (Multiply
      (Constant 2)
      (Variable "x"))
    (Constant 3)))
```

Функция `(evaluate expression vars)` должна производить вычисление выражения expression для значений переменных, заданных отображением vars. Например, `(evaluate expr {"x" 2})` должно быть равно 1.
Функция `(toString expression)` должна выдавать запись выражения в стандартной для Clojure форме.
Функция `(parseObject "expression")` должна разбирать выражения, записанные в стандартной для Clojure форме. Например,
`(parseObject "(- (* 2 x) 3)")`
должно быть эквивалентно expr.
Функция `(diff expression "variable")` должена возвращать выражение, представляющее производную исходного выражения по заданой пермененной. Например, (diff expression "x") должен возвращать выражение, эквивалентное `(Constant 2)`, при этом выражения `(Subtract (Constant 2) (Constant 0))` и
```
(Subtract
  (Add
    (Multiply (Constant 0) (Variable "x"))
    (Multiply (Constant 2) (Constant 1)))
  (Constant 0))
```
                    
так же будут считаться правильным ответом.
Сложный вариант. Констуркторы Add, Subtract, Multiply и Divide должны принимать произвольное число аргументов. Разборщик так же должен допускать произвольное число аргументов для +, -, *, /.
При выполнение задания можно использовать любой способ преставления объектов.

Сложный вариант. Реализуйте функцию (parseObjectInfix "expression"), разбирающую выражения, записанные в инфиксной форме, и функцию toStringInfix, возвращающую строковое представление выражения в этой форме. Например,
```
(toStringInfix (parseObjectInfix "2 * x - 3"))
```
должно возвращать ((2 * x) - 3).
Функции разбора должны базироваться на библиотеке комбинаторов, разработанной на лекции.

## [Prolog](https://github.com/Ilya-Sib/expression-parsers/tree/main/PrologDCGParser)
# Задание

Доработайте правило `(evaluate Expression Varsiables Result)`, вычисляющее арифметические выражения.
Пример вычисления выражения 2x-3 для x = 5:
```
eval(
    operation(op_subtract,
        operation(op_multiply,
            const(2),
            variable(x)
        ),
        const(3)
    ),
    [(x, 5)],
    7
)
```                 
Поддерживаемые операции: сложение `(op_add, +)`, вычитание `(op_subtract, -)`, умножение `(op_multiply, *)`, деление `(op_divide, /)`, противоположное число `(op_negate, negate)`.
Реализуйте правило `(infix_str Expression Atom)`, разбирающее/выводящее выражения, записанные в полноскобочной инфиксной форме. Например,
```
infix_str(
    operation(op_subtract,operation(op_multiply,const(2),variable(x)),const(3)),
    '((2 * x) - 3)'
)
```
Правила должны быть реализованы с применением DC-грамматик.
