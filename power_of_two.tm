states 6

check_at_least_1
first_odd
even
odd
go_back

accept +

alphabet 2 1 #

check_at_least_1 1 first_odd _ R

first_odd # first_odd # R
first_odd 1 even # R
first_odd _ accept _ S

even 1 odd # R
even # even # R
even _ go_back _ L
odd 1 even 1 R
odd # odd # R

go_back 1 go_back 1 L
go_back # go_back # L
go_back _ first_odd _ R