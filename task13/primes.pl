not_prime(N) :- not_prime_table(N).
min_div(N, A) :- min_div_table(N, A).
fill_prime(K, P) :-
   NUM is K * P,
   NUM > 1000,
   !.

fill_prime(K, P) :-
    NUM is K * P,
    NUM =< 1000,
    (not(not_prime(NUM)) -> assert(not_prime_table(NUM)); true),
    (not(min_div(_, NUM)) -> assert(min_div_table(P, NUM)); true),
    K1 is K + 1,
    fill_prime(K1, P).

new_loop(K) :- K > 1000, !.

new_loop(K) :-
   K =< 1000,
   fill_prime(2, K),
   NEXTK is K + 1,
   new_loop(NEXTK).

prime(N) :-
   not(not_prime(N)).

composite(N) :- not(prime(N)).

init(1000) :- new_loop(2), assert(not_prime_table(1)).


concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).
concat(B, [], [B]).

is_min(A, B) :-
   min_div(A, B).

is_min(A, A) :-
   prime(A), !.

prime_divisors(C, []) :- C is 1.
prime_divisors(C, [H | T]) :- is_min(H, C), C1 is div(C, H), prime_divisors(C1, T).
