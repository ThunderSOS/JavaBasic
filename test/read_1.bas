10 DIM a[10]
11 LET a[1] = 100
15 DATA 15,25,35,45,55,65,75,85,95,105

20 FOR n = 0 TO 9 STEP 2
30 READ a, b
31 LET a[n] = a
32 LET a[n+1] = b
34 NEXT n

40 FOR n = 0 TO 9
41 PRINT a[n]
42 NEXT n

