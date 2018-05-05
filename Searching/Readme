The project - A program that provides a valid placement of lizards in a nursery.<br />
<br/>The program can compute a valid placement( if it exists) in under 30secs for a board size upto 50x50.

-----------------------------------------------------------------------------------------------------------------------
---The Problem---<br />
Find a place to put each baby lizard in a nursery.
However, there is a catch, the baby lizards have very long tongues. A baby lizard can shoot out
its tongue and eat any other baby lizard before you have time to save it. As such, you want to
make sure that no baby lizard can eat another baby lizard in the nursery.
For each baby lizard, you can place them in one spot on a grid. From there, they can shoot out
their tongue up, down, left, right and diagonally as well. Their tongues are very long and can
reach to the edge of the nursery from any location.<br/>
In addition to baby lizards, your nursery may have some trees planted in it. Your lizards cannot
shoot their tongues through the trees nor can you move a lizard into the same place as a tree. As
such, a tree will block any lizard from eating another lizard if it is in the path. Additionally, the
tree will block you from moving the lizard to that location.<br/><br/>
---Input---<br/>
The file input.txt in the current directory of your program will be formatted as follows:<br/>
First line: instruction of which algorithm to use: BFS, DFS or SA<br/>
Second line: strictly positive 32-bit integer n, the width and height of the square nursery<br/>
Third line: strictly positive 32-bit integer p, the number of baby lizards<br/>
Next n lines: the n x n nursery, one file line per nursery row (to show you where the trees are).
It will have a 0 where there is nothing, and a 2 where there is a tree.<br/><br/><br/>

---Output---<br/>
The file output.txt which your program creates in the current directory should be
formatted as follows:<br/>
First line: OK or FAIL, indicating whether a solution was found or not.<br/>
Next n lines: the n x n nursery, one line in the file per nursery row, including the baby lizards
and trees. It will have a 0 where there is nothing, a 1 where you placed a baby
lizard, and a 2 where there is a tree.<br/>

Example 3:<br/>
For this input.txt :<br/>
SA<br/>
8<br/>
9<br/>
00000000<br/>
00000000<br/>
00000000<br/>
00002000<br/>
00000000<br/>
00000200<br/>
00000000<br/>
00000000<br/>
one possible correct output.txt is:<br/>
OK<br/>
00000100<br/>
10000000<br/>
00001000<br/>
01002001<br/>
00001000<br/>
00100200<br/>
00000010<br/>
00010000<br/>
