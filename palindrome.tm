states 9
start

going_to_end_0
going_to_end_1
going_to_end_2

end_0
end_1
end_2

going_to_start

accept +

alphabet 3 0 1 2

start 0 going_to_end_0 _ R
start 1 going_to_end_1 _ R
start 2 going_to_end_2 _ R
start _ accept _ S

going_to_end_0 0 going_to_end_0 0 R
going_to_end_0 1 going_to_end_0 1 R
going_to_end_0 2 going_to_end_0 2 R
going_to_end_0 _ end_0 _ L

going_to_end_1 0 going_to_end_1 0 R
going_to_end_1 1 going_to_end_1 1 R
going_to_end_1 2 going_to_end_1 2 R
going_to_end_1 _ end_1 _ L

going_to_end_2 0 going_to_end_2 0 R
going_to_end_2 1 going_to_end_2 1 R
going_to_end_2 2 going_to_end_2 2 R
going_to_end_2 _ end_2 _ L

end_0 0 going_to_start _ L
end_1 1 going_to_start _ L
end_2 2 going_to_start _ L

end_0 _ accept _ S
end_1 _ accept _ S
end_2 _ accept _ S

going_to_start 0 going_to_start 0 L
going_to_start 1 going_to_start 1 L
going_to_start 2 going_to_start 2 L

going_to_start _ start _ R