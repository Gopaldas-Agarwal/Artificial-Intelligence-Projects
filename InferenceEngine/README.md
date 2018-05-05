The project - A resolution engine that can deduce queries given a knowledge base, using first-order logic resolution(https://en.wikipedia.org/wiki/Resolution_(logic)#Resolution_in_first_order_logic).

The resulting engine is able to answer upto 250 questions for a knowledge base with 1000 statements.

-----------------------------------------------------------------------------------------------------------------------
---The knowledge Base---
The knowledge base contains sentences with the following operators:

NOT X   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;     ~X<br />
X OR Y &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;      X | Y


---Input---<br />
<NQ = NUMBER OF QUERIES><br />
<QUERY 1><br />
…<br />
<QUERY NQ><br />
<NS = NUMBER OF GIVEN SENTENCES IN THE KNOWLEDGE BASE><br />
<SENTENCE 1><br />
…<br />
<SENTENCE NS><br />
where
• Each query will be a single literal of the form Predicate(Constant) or ~Predicate(Constant).
• Variables are all single lowercase letters.
• All predicates (such as Sibling) and constants (such as John) are case-sensitive alphabetical strings that
begin with an uppercase letter.
• Each predicate takes at least one argument. Predicates will take at most 100 arguments. A given
predicate name will not appear with different number of arguments.

---Output---<br />
For each query, determine if that query can be inferred from the knowledge base or not, one query per line:
<ANSWER 1><br />
…<br />
<ANSWER NQ><br />


Example:<br />
For this input.txt:<br />
6<br />
F(Joe)<br />
H(John)<br />
~H(Alice)<br />
~H(John)<br />
G(Joe)<br />
G(Tom)<br />
14<br />
~F(x) | G(x)<br />
~G(x) | H(x)<br />
~H(x) | F(x)<br />
~R(x) | H(x)<br />
~A(x) | H(x)<br />
~D(x,y) | ~H(y)<br />
~B(x,y) | ~C(x,y) | A(x)
B(John,Alice)<br />
B(John,Joe)<br />
~D(x,y) | ~Q(y) | C(x,y)<br />
D(John,Alice)<br />
Q(Joe)<br />
D(John,Joe)<br />
R(Tom)<br />
<br />
Output.txt will be:<br />
FALSE<br />
TRUE<br />
TRUE<br />
FALSE<br />
FALSE<br />
TRUE<br />
