lookup(K, [(K1, V) | _], V) :- atom_chars(K, [K1 | _]).
lookup(K, [_ | T], V) :- lookup(K, T, V).

operation(op_negate, A, R) :- R is -A.
operation(op_sinh, A, R) :- R is ((exp(A) - exp(-A)) / 2).
operation(op_cosh, A, R) :- F is exp(A), S is exp(-A), Add is F + S, R is Add / 2.
operation(op_add, A, B, R) :- R is A + B.
operation(op_subtract, A, B, R) :- R is A - B.
operation(op_multiply, A, B, R) :- R is A * B.
operation(op_divide, A, B, R) :- R is A / B.

evaluate(const(V), _, V).
evaluate(variable(Name), Vars, R) :- lookup(Name, Vars, R).
evaluate(operation(Op, A), Vars, R) :-
    evaluate(A, Vars, AV),
    operation(Op, AV, R).
evaluate(operation(Op, A, B), Vars, R) :- 
    evaluate(A, Vars, AV), 
    evaluate(B, Vars, BV), 
    operation(Op, AV, BV, R).

:- load_library('alice.tuprolog.lib.DCGLibrary').

nonvar(V, T) :- var(V).
nonvar(V, T) :- nonvar(V), call(T).

num_chars('-', ['-']) :- !.
num_chars(Value, Chars) :- number_chars(Value, Chars).

expr_p(variable(Name)) --> 
    { nonvar(Name, atom_chars(Name, Chars)) },
    ws, variable_p(Chars), ws,
    { Chars = [_ | _], atom_chars(Name, Chars) }.

variable_p([]) --> [].
variable_p([H | T]) --> 
    { member(H, [x, y, z, 'X', 'Y', 'Z'])},
    [H], variable_p(T).

expr_p(const(Value)) --> 
    { nonvar(Value, num_chars(Value, Chars)) },
    ws, digits_p(Chars), ws,
    { Chars = [_ | _], num_chars(Value, Chars) }.

digits_p([]) --> [].
digits_p([H | T]) --> 
    { member(H, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-'])},
    [H], digits_p(T).

op_p(op_negate) --> [n, e, g, a, t, e].
op_p(op_sinh) --> [s, i, n, h].
op_p(op_cosh) --> [c, o, s, h].
op_p(op_add) --> ['+'].
op_p(op_subtract) --> ['-'].
op_p(op_multiply) --> ['*'].
op_p(op_divide) --> ['/'].

ws --> [].
ws --> [' '], ws.

expr_p(operation(Op, A)) --> ws, op_p(Op), ws, ['('], expr_p(A), [')'], ws.
expr_p(operation(Op, A, B)) --> ws, ['('], ws, expr_p(A), [' '], ws, op_p(Op), [' '], ws, expr_p(B), ws, [')'], ws.

infix_str(E, A) :- ground(E), phrase(expr_p(E), C), atom_chars(A, C), !.
infix_str(E, A) :- atom(A), atom_chars(A, C), phrase(expr_p(E), C), !.
