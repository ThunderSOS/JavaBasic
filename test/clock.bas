1 LET s_width = s_width()/2 : LET s_height = s_height()/2
2 WINDOW "Clock", s_width, s_height
3 INK 255, 255, 255 : PAPER 0, 0, 0: CLS

10 DEF seconds()=INT(local_time()/1000)   
11 DEF minutes()=INT(seconds()/60)
12 DEF hours()=INT(minutes()/12)

# draw the numbers on the clock face
20 PLOT s_width/2, s_height/2 
21 LET radius = 100  
22 LET tradius = radius -11
30 FOR n = 0 TO 59 STEP 5
31 LET x = s_width/2+tradius*(sin(-(n+30)/30*PI))-5: LET y = s_height/2+tradius*(cos(-(n+30)/30*PI))+5
32 PRINT AT x, y; INT(n/5)
33 NEXT n
40 LET draw = 1
45 ELLIPSE radius*2, radius*2

# clock loop
50 LET t = seconds()
55 LET secs = mod(t, 60)
56 LET mins = mod(minutes(), 60)
57 LET hours = mod(hours(), 24)
60 PUSH secs
61 GOSUB 150
62 POP secs$
63 PUSH mins
64 GOSUB 150
65 POP mins$
66 PUSH hours/6
67 GOSUB 150
68 POP hours$
70 PRINT AT 0, 20; hours$ + ":" + mins$ + ":" + secs$

# draw hands
89 INK 255, 255, 0
90 DRAW s_width/2+radius*(sin(-(secs+30)/30*PI)), s_height/2+radius*(cos(-(secs+30)/30*PI)) 
92 INK 0, 255, 255
95 IF secs == 0 && draw == 0 THEN DRAW s_width/2+80*(sin(-(mins+30)/30*PI)), s_height/2+80*(cos(-(mins+30)/30*PI)) 
96 INK 0, 255, 0
97 IF secs == 0 && draw == 0 THEN DRAW s_width/2+60*(sin(-(hours+30)/30*PI)), s_height/2+60*(cos(-(hours+30)/30*PI)) 
98 INK 255, 255, 255

100 PAUSE 100
110 LET t1 = seconds()
120 IF t1 == t THEN GOTO 100

# draw hour and minute on the first pass
124 INK 0, 255, 255
125 IF draw == 1 THEN DRAW s_width/2+80*(sin(-(mins+30)/30*PI)), s_height/2+80*(cos(-(mins+30)/30*PI)) 
126 INK 0, 255, 0
127 IF draw == 1 THEN DRAW s_width/2+60*(sin(-(hours+30)/30*PI)), s_height/2+60*(cos(-(hours+30)/30*PI)) 
128 LET draw = 0

# undraw the hands 
129 INK 255, 255, 0
130 DRAW s_width/2+radius*(sin(-(secs+30)/30*PI)), s_height/2+radius*(cos(-(secs+30)/30*PI))
132 INK 0, 255, 255 
133 IF mod(t1, 60) == 0 THEN DRAW s_width/2+80*(sin(-(mins+30)/30*PI)), s_height/2+80*(cos(-(mins+30)/30*PI)) 
134 IF mod(t1, 60) == 0 THEN DRAW s_width/2+60*(sin(-(hours+30)/30*PI)), s_height/2+60*(cos(-(hours+30)/30*PI)) 

# jump back to draw the new second
140 GOTO 50

# subroutine to pad an int with zeroes
150 POP x
160 LET a$ = "" + INT(x)
170 IF x < 10 THEN LET a$ = "0" + a$
180 PUSH a$
190 RETURN