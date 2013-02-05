using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _09_Most_frequent_number_in_an_array
{
    //Write a program that finds the most frequent number in an array. Example:
	//{4, 1, 1, 4, 2, 3, 4, 4, 1, 2, 4, 9, 3}  4 (5 times)
    class MostFrequentNumber
    {
        static void Main(string[] args)
        {
            string str = Console.ReadLine();
            string[] input = str.Split(' ');
            int[] arr = new int[input.Length];
            int[] flagArr = new int[arr.Length];
            int k;
            int number = 0;
            int index = 0;

            for (int i = 0; i < arr.Length; i++)
            {
                arr[i] = int.Parse(input[i]);
            }

            for (int i = 0; i < arr.Length - 1; i++)
            {
                if (flagArr[i] == 0)
                {
                    k = 0;
                    for (int j = i + 1; j < arr.Length; j++)
                    {
                        if (arr[i] == arr[j])
                        {
                            k++;
                            flagArr[j] = k;
                        }
                    }
                }
            }

            for (int i = 0; i < flagArr.Length; i++)
            {
                if (number < flagArr[i])
                {
                    number = flagArr[i];
                    index = i;
                }
            }
            Console.WriteLine("Most common number is: " + arr[index]);
        }
    }
}


/*
4 1 1 4 2 3 4 4 1 2 4 9 3
 */ 