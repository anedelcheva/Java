Huffman Compression Algorithm
--------------------------------------------------------------------------
The Huffman Compression Algorithm is not implemented fully. *JobSplitter.java* contains only the code for calculating a frequency table for a given file. JobSplitter.java calculates the occurrence of symbols in the ASCII table and computes a frequency table. The work can be split to multiple threads. The user can execute the program in the following way:

        $ java JobSplitter -f <filepath> -t[|-tasks|tasks] <number_of_threads> [-q|-quiet|quiet]
    
JobSplitter can be run with N number of threads if a machine has N cores. There is a speedup in time up to 6-7 number of threads. A file is read to a string and the filecontent of the string is split to an array of strings where the array has length *<number_of_threads>*. Then every thread calculates a frequency table for a part if the file. The results from the different threads are merged to a common frequency table.
