
syntax keyword rep repeat until for
syntax keyword cond if else
syntax keyword struct record 
syntax keyword todoo TODO 
syntax keyword bool false true False True
syntax keyword func function return
syntax keyword kw start println len const new
syntax keyword typee int float bool double string char void 

"syntax keyword operat + - \[ \] 

syntax region coment start="//" end="\n"
syntax region str start="\"" end="\""
syntax region chr start="\'" end="\'"

syn match id '[A-Za-z0-9_]\+'

syn match celNumber '\d\+'
syn match celNumber '[-+]\d\+'
syn match celNumber '[-+]\d\+\.\d*'
syn match celNumber '[-+]\=\d[[:digit:]]*[eE][\-+]\=\d\+'
syn match celNumber '\d[[:digit:]]*[eE][\-+]\=\d\+'
syn match celNumber '[-+]\=\d[[:digit:]]*\.\d*[eE][\-+]\=\d\+'
syn match celNumber '\d[[:digit:]]*\.\d*[eE][\-+]\=\d\+'


highlight link cond Conditional
highlight link rep Repeat
highlight link str String
highlight link chr Character
highlight link struct Structure
highlight link todoo TODO 
highlight link bool Boolean 
highlight link operat Operator
highlight link func Include
highlight link celNumber Number
highlight link coment Comment
highlight link kw Keyword
highlight link typee Type
highlight link id Identifier


