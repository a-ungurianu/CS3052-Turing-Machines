states 6

q1
q2
q3
q4
q5

accept +

alphabet 2 0 x

q1 0 q2 _ R

q2 x q2 x R
q2 0 q3 x R
q2 _ accept _ S

q3 0 q4 x R
q3 x q3 x R
q3 _ q5 _ L

q4 0 q3 x R
q4 x q4 x R

q5 0 q5 0 L
q5 x q5 x L
q5 _ q2 _ R