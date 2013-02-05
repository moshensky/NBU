using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _15_Prime_numbers___Sieve_of_Eratosthenes
{
    //Write a program that finds all prime numbers in the range [1...10 000 000].
    //Use the sieve of Eratosthenes algorithm (find it in Wikipedia).
    class SieveEratosthenesPrimeNumbers
    {
        static void Main(string[] args)
        {
            int n = 10000000;
            int[] arr = new int[n + 1];
            bool[] flag = new bool[n + 1];

            for (int i = 2; i <= n; i++)
            {
                arr[i] = i;
                flag[i] = true;
            }

            int p = 2;

            for (int i = 2; i <= Math.Sqrt(n); i++)
            {
                if (flag[i])
                {
                    for (int j = i * i; j <= n; j += i)
                    {
                        flag[j] = false;
                    }
                }
            }

            for (int i = 2; i < flag.Length; i++)
            {
                if (flag[i])
                    Console.WriteLine(arr[i]);
            }

        }
    }
}
