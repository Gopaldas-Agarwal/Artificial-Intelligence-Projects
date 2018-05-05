The project - A resolution engine that can deduce queries given a knowledge base, using first-order logic resolution(https://en.wikipedia.org/wiki/Resolution_(logic)#Resolution_in_first_order_logic).

The resulting engine is able to answer upto 250 questions for a knowledge base with 1000 statements.

-----------------------------------------------------------------------------------------------------------------------
---The knowledge Base---
The knowledge base contains sentences with the following operators:

NOT X          ~X
X OR Y         X | Y


---Input---
<NQ = NUMBER OF QUERIES>
<QUERY 1>
…
<QUERY NQ>
<NS = NUMBER OF GIVEN SENTENCES IN THE KNOWLEDGE BASE>
<SENTENCE 1>
…
<SENTENCE NS>
where
• Each query will be a single literal of the form Predicate(Constant) or ~Predicate(Constant).
• Variables are all single lowercase letters.
• All predicates (such as Sibling) and constants (such as John) are case-sensitive alphabetical strings that
begin with an uppercase letter.
• Each predicate takes at least one argument. Predicates will take at most 100 arguments. A given
predicate name will not appear with different number of arguments.

---Output---
For each query, determine if that query can be inferred from the knowledge base or not, one query per line:
<ANSWER 1>
…
<ANSWER NQ>


Example:
For this input.txt:
6
F(Joe)
H(John)
~H(Alice)
~H(John)
G(Joe)
G(Tom)
14
~F(x) | G(x)
~G(x) | H(x)
~H(x) | F(x)
~R(x) | H(x)
~A(x) | H(x)
~D(x,y) | ~H(y)
~B(x,y) | ~C(x,y) | A(x)
B(John,Alice)
B(John,Joe)
~D(x,y) | ~Q(y) | C(x,y)
D(John,Alice)
Q(Joe)
D(John,Joe)
R(Tom)

Output.txt will be:
FALSE
TRUE
TRUE
FALSE
FALSE
TRUE
