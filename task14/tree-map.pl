newnode(Key, Val, L, R, node(Key, Val, L, R)).

insert(node(Key1, Val1, L1, R1), node(Key2, Val2, L2, R2), R) :-
    Key2 < Key1,
    newnode(Key2, Val2, L2, R2, LNEW),
    insert(L1, LNEW, LNEWS),
    newnode(Key1, Val1, LNEWS, R1, R).

insert(node(Key1, Val1, L1, R1), node(Key2, Val2, L2, R2), R) :-
    Key1 < Key2,
    newnode(Key2, Val2, L2, R2, RNEW),
    insert(R1, RNEW , RNEWS),
    LLAST is L1,
    newnode(Key1, Val1, L1, RNEWS, R).

insert(0, node(Key2, Val2, L2, R2), R) :-
   newnode(Key2, Val2, L2, R2, R).

insert(node(Key2, Val2, L2, R2), 0, R) :-
   newnode(Key2, Val2, L2, R2, R).

map_get(node(Key, Val ,L, R), Fkey, Fval) :-
    Key < Fkey,
    map_get(R, Fkey, Fval),
    !.

map_get(node(Key, Val, L ,R), Fkey, Fval) :-
   Key > Fkey,
   map_get(L, Fkey, Fval),
   !.

map_get(node(Fkey, Fval, L, R), Fkey, Fval) :-
   !.


mbw([], node(K, V, L, RE), R) :- newnode(K, V, L, RE, R).
mbw([], 0, R) :- R is 0.
mbw([(A, B) | T], R1, R):-
   newnode(A, B, 0, 0, Temp),
   insert(R1, Temp, R2),
   mbw(T, R2, R).



map_build(L, R) :-
   print(L), nl,
   mbw(L, 0, R).