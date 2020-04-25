import json
from os import system as shelll
from os import remove as rm 

# get ready files 
file_name = "START.pgs"

other_branch = input("which other branch you want to merge? ").strip()

temp_name = f"{other_branch}_{file_name}"

command = f"git show {other_branch}:{file_name} > {temp_name}"
shelll(command)


# load files 
main_text = open(file_name).read()
main_list = json.loads(main_text)
#print(main_list)

other_text = open(temp_name).read() 
other_list = json.loads(other_text)
#print(other_list)



# merge files 

all_set = set(str(json.dumps(a, indent =4))  for a in  (main_list + other_list) )

merged_list = list(all_set)


# write down files
final_text = str(json.dumps(merged_list, indent = 4))
open(file_name,"w").write(final_text)


# clean temp file
rm(temp_name)



