import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;

public class ram
{
	private static final int ASCII_start = 32; //first ASCII char " ", also excess
	private static final int ASCII_count = 95; // count of ASCII chars, ASCII_start + ASCII_count = last ASCII char "~"

    static double start = System.currentTimeMillis();

	//method to decrypt inputFile
	private static void decrypt(int[] decryptKey, File inputFile, File outputFile)
	{
		try
		{
			//initialize IO classes
			Scanner inputFileReader = new Scanner(inputFile);
			inputFileReader.useDelimiter("");
			BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputFile, true));

			int currentOutputCharIndex = 0;
			while(inputFileReader.hasNext())
			{
				//read next char from inputFile
				char currentInputChar = inputFileReader.next().charAt(0);
				char currentOutputChar;

				//skip enter to not be decryptet
				if(currentInputChar != 13 && currentInputChar != 10)
				{
					//encryption of currenInputChar to currentOutputChar, writing to outputFile
					int currentKeyAscii = decryptKey[(currentOutputCharIndex%decryptKey.length)];
					currentOutputChar = (char) ((currentInputChar-currentKeyAscii+ASCII_count)%ASCII_count+ASCII_start);
					currentOutputCharIndex++;
					outputFileWriter.write(currentOutputChar);
				}
				//append enter after decryption
				else if(currentInputChar == 13)
				{
					outputFileWriter.newLine();
				}
			}

			//print current key next to output
			char[] charKey = new char[decryptKey.length];
			for(int i = 0; i < decryptKey.length; i++)
			{
				charKey[i] = (char) decryptKey[i];
			}
			outputFileWriter.write(" --> "+Arrays.toString(charKey));
			outputFileWriter.newLine();

			outputFileWriter.close();
			System.out.println("cracking File....");

		} catch(IOException e)
		{
			System.out.println("\nERROR: could not open file");
		}
	}

	//fucking black magic
	private static void iterateKey(int keyLength, File inputFile, File outputFile)
	{
		resetFile(outputFile);
		int[] counter = new int[keyLength];
		while(true)
		{
			//create current key
			int[] key = new int[keyLength];
			for(int i = 0; i < keyLength; i++)
			{
				key[i] = ASCII_start+counter[i];
			}

			//decrypt inputFile with current key
			decrypt(key, inputFile, outputFile);

			//calculating current position for key
			int pos = keyLength-1;
			while(pos >= 0 && ++counter[pos] == ASCII_count)
			{
				counter[pos] = 0;
				pos--;
			}

			//exit while loop
			if(pos < 0)
			{
				break;
			}
		}
	}

	private static void iterateKeyLength(int maxKeyLength, File inputFile, File outputFile)
	{
		for(int i = 1; i <= maxKeyLength; i++)
		{
			iterateKey(i, inputFile, outputFile);
		}

        //programm ends here

        double finish = System.currentTimeMillis();
        double timeElapsed = (finish - start) / 1000;
        System.out.println("the duration of this bruteforce attempt was: " +  timeElapsed + " seconds");
	}

	private static int[] stringToASCII(String plainText)
	{
		int length = plainText.length();
		int[] plainASCII = new int[length];
		for(int i = 0; i < length; i++)
		{
			plainASCII[i] = plainText.charAt(i);
		}
		return plainASCII;
	}

	public static void parseKeyFile(File keyFile, File inputFile, File outputFile)
	{
		resetFile(outputFile);
		try
		{
			Scanner inputFileReader = new Scanner(keyFile);
			while(inputFileReader.hasNextLine())
			{
				String currentLine = inputFileReader.nextLine();
				decrypt(stringToASCII(currentLine), inputFile, outputFile);
			}
		}
        catch(IOException e)
		{
			System.out.println("\nERROR: could not open file");
		}
	}


	//creates workFile if it doesn't exist, if it exists it is reset
	public static void resetFile(File workFile)
	{
		try
		{
			FileWriter reset = new FileWriter(workFile);
			reset.write("");
			reset.close();
		} catch(IOException e)
		{
			System.out.println("\nERROR: could not open file");
		}
	}



    private static void helpPage()
    {
        System.out.println("\n----------------------------------------------------------------------------------------");
        System.out.println("This is the help-page of ram");
        System.out.println("----------------------------------------------------------------------------------------");
        System.out.println("ram can bruteforce files encrypted with the caesar- or viginere-cypher");
        System.out.println("some examples for the usage of ram are:");
        System.out.println();
        System.out.println("ram -l 3 -i myFileToBruteForce.txt -o myOutputFile.txt");
        System.out.println("ram -l 5");
        System.out.println("ram -f myPasswordFile.txt -i myFileToBruteForce.txt");
        System.out.println("ram -f myPasswordFile.txt -o myOutputFile.txt");
        System.out.println("----------------------------------------------------------------------------------------");

        System.out.println("explanation of each argument:");
        System.out.println();
        System.out.println("-l or -length: the max length of the password you want to bruteforce");
        System.out.println("warning: the higher this parameter is set the more combinations are tried");
        System.out.println("    -l 1 --> 95 combinations");
        System.out.println("    -l 2 --> 9025 combinations");
        System.out.println("    -l 3 --> 857.375 combinations");
        System.out.println("    -l 4 --> 81.450.625 combinations");
        System.out.println("    -l 5 --> 7.737.809.375 combinations --> very time consuming");
        System.out.println("-f or -file: a file with potential passwords to be tried");
        System.out.println("one argument -l or -f is required, both can't be used simultaneously");
        System.out.println("-i or -input: is the input file, if not set, input.txt is used as input");
        System.out.println("-o or -output: is the output file, if not set, output.txt is used as output");
        System.out.println("-h or -help: opens this page, overwrites all other arguments");
        System.out.println("----------------------------------------------------------------------------------------");
    }

	public static void main(String[] args)
	{

		int maxKeyLength = 0;
		String inputPath = null;
		String outputPath = null;
		String passwordsPath = null;

		for(int i = 0; i < args.length; i++)
		{
			switch(args[i])
			{
				case "-l", "-length":
					if(args.length-i > 1)
					{
						maxKeyLength = Integer.parseInt(args[++i]);
						break;
					}
				case "-f", "-file":
					if(args.length-i > 1)
					{
						passwordsPath = args[++i];
						break;
					}
				case "-i", "-input":
					if(args.length-i > 1)
					{
						inputPath = args[++i];
						break;
					}
				case "-o", "-output":
					if(args.length-i > 1)
					{
						outputPath = args[++i];
						break;
					}
                case "-h", "-help":
                    helpPage();
                    System.exit(0);
				default:
				{
					System.out.println("\nERROR: invalid command");
					System.exit(0);
				}

			}
		}

		File inputFile = new File("input.txt");
		File outputFile = new File("output.txt");
		if(inputPath != null)
		{
			inputFile = new File(inputPath);
		}
		if(outputPath != null)
		{
			outputFile = new File(outputPath);
		}
		if(maxKeyLength > 0 && passwordsPath != null)
		{
			System.out.println("ERROR: parameter -l and -f are both used");
		}
		else if(maxKeyLength > 0 && passwordsPath == null)
		{
			//##########Add warning if maxKeyLength > 4##########
            if(maxKeyLength > 4)
            {
                System.out.println("WARNING: you want to try more than 7.737.809.375 password combinations, which can be very time consuming");
                System.out.println("enter 'Y' to proceed");
                Scanner scanner = new Scanner(System.in);
                String confirm = scanner.nextLine();
                if(confirm.equals("Y"))
                {
                    iterateKeyLength(maxKeyLength, inputFile, outputFile);
                }
                else
                {
                    System.exit(0);
                }
            }
            else
            {
                iterateKeyLength(maxKeyLength, inputFile, outputFile);
            }


		}
		else if(passwordsPath != null)
		{
			parseKeyFile(new File(passwordsPath), inputFile, outputFile);
		}
        else if (inputPath==null && outputPath==null && passwordsPath==null && maxKeyLength==0)
        {
            helpPage();
        }
        else
		{
			System.out.println("ERROR: invalid command");
		}
	}
}

