 5 OPEN HTTP a; "http://google.com", 80
10 LET b$ = ""
11 FOR n = 1 TO 100
15 READ FROM a, a; (n-1)*100, n*100
20 FOR m = 0 TO 99
23 LET b$ = b$ + CHR$(a[m])
24 NEXT m
25 NEXT n
30 PRINT b$

