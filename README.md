# ram

## what is ram

ram is a tool to bruteforce encrypted files using the caesar or viginere cypher.

## how does it work

ram can use two differenct methods for bruteforcing the enccrypted file.

  1. iterating through every possible password:
       When using this method ram will try every possible combinations of printable ASCII chars as a possible password. Firstly ram will try all passwords with length 1 (caesar cypher) starting with " " (ASCII code 32) and ending with "\~" (ASCII code 126).
Then it will proceed to try all passwords length 2. This time the passwords will have the first digit " " - "~", but for each char in the first digit ram will iterate through all ASCII chars again. Ram will continue to add new digits until it reaches the max password length which also has to be given as a argument.

  2. parsing through a set password list:
      This mode parses through a .txt file with plausible passwords and will try each to decypher the input file. The passworld list has to be given to ram as a parameter.

     
Depending on the used method, ram will then decrypt the input file witch each possible password, and store the text of the decrypted input file and the corresponding password in the output file. After ram is finished, the user can check the output file and search for a line with readable text, the corresponding password is the password used to encrypt the file.

for example: 
There is a encrypted file containing `Yw}~!2h"$~u3`. Which has to be bruteforced. To crack this file we will use the first method with the maximal password length being 2. ram will iterate through all possible passwords, encrypt the file and save the encrypted text and the used password in the output file. 

the output file will have contents following this scheme:

`Yw}~!2h"$~u3 --> [ ,  ]`

`Yv}}!1h!$}u2 --> [ , !]`

`Yu}|!0h $|u1 --> [ , "]`

...

`Yy}!!4h$$!u5 --> [ , }]`

`Yx} !3h#$ u4 --> [ , ~]`

`Xw|~ 2g"#~t3 --> [!,  ]`

`Xv|} 1g!#}t2 --> [!, !]`

...

`Xy|! 4g$#!t5 --> [!, }]`

`Xx|  3g## t4 --> [!, ~]`

`Ww{~~2f""~s3 --> [",  ]`

`Wv{}~1f!"}s2 --> [", !]`

...

...

`Hflmo!Wprmd" --> [1, 1]`

`Hello World! --> [1, 2]`

`Hdlko~Wnrkd  --> [1, 3]`

...

...

`Zz~""5i%%"v6 --> [~, |]`

`Zy~!"4i$%!v5 --> [~, }]`

`Zx~ "3i#% v4 --> [~, ~]`

Althoug the real output file is much longer it can easily be seen that the correctly decrypted test is "Hello World!" which was encrypted using the password "12".


## installation

In order to install ram download the folder conainting the `ram.bat` and `ram.jar` files to the directory where you want to save them. Then edit `ram.bat` and change the path to the path of `ram.jar`. The tool is now ready to be used. But if you want to use it from any terminal and not only inside the folder you have to add the path of the folder to the PATH enviorment variable. Then you can use ram from anywhere in your system by writing uncrackable in the console.

## usage

This section will cover the syntax and usage of uncrackable.

the uncrackable command syntax is following:

`ram <mode> <inputfilePath> <outputfilePath>`

with examples being:

`ram -l 3 -i myFileToBruteForce.txt -o myOutputFile.txt`
`ram -l 5`
`ram -f myPasswordFile.txt -i myFileToBruteForce.txt`
`ram -f myPasswordFile.txt -o myOutputFile.txt`


explanation of parameters:
1. \<mode\>
   
   can either be set to length or file
   
	  -l or -length: specifies that the inputFile has to be bruteforced using the method of iterating throug all possible ASCII char combinations
example `-l 2` will try all passwords up to length 2.
   
	  -f or -file: specifies that the inputFile has to be bruteforced by parsing through a password list.
example `-f myPasswordList.txt`
   
	One of these paramters has to be set. The programm won't work if neither or both are set.
   
4. <inputfilePath\>
   
   -i or -input: specifies the file to be bruteforced.
   
   If file is in current active directory just the name suffices if the file is located outside of the AD the absolute path has to be given.
   
   If not set the file `input.txt` in the AD is used.
   
5. <outputfilePath\>
   
   -o or -output: specifies the file to which the output is written.
   
    If file is in current active directory the name suffices if the file is located outside of the AD the absolute path has to be given.
   
    Will create the file with the given path if it doesn't exist.
   
	If not set the file `output.txt` in the AD is used or will be created.
	
 7. <help\>
    
    -h or -help: will print the helppage
    
    this parameter overwrites all other parameters set
    
    helppage will also open if no parameter is set
