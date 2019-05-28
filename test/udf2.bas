# Test multi variable UDF's
10 DEF h_squared(x, y) = x*x + y*y
15 DEF pythagorean_length(x, y) = SQR(h_squared(x, y))
20 LET a = 3 : LET b = 4
30 LET z = pythagorean_length(a, b)
40 PRINT z
# Hopefully we printed 5.0