using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _14_Quick_Sort
{
    //Write a program that sorts an array of strings using the quick sort algorithm
    class QuickSort
    {
        static void Main(string[] args)
        {
            int[] arr = new int[] {2, 8, 7, 1, 3, 5, 6, 4 };
            QuickSortRecursion(arr, 0, arr.Length-1);
            for (int i = 0; i < arr.Length; i++)
            {
                Console.Write(arr[i] + " ");
            }
            Console.WriteLine();
        }

        static void QuickSortRecursion(int[] arr, int p, int r)
        {
            if (p < r)
            {
                int q = Partition(arr, p, r);
                QuickSortRecursion(arr, p, q - 1);
                QuickSortRecursion(arr, q + 1, r);
            }
        }

        private static int Partition(int[] arr,int p,int r)
        {
            int pivot = arr[r];
            int i = p-1;

            for (int j = p; j < r; j++)
			{
                if (arr[j] <= pivot)
                {
                    i = i + 1;
                    Exchange(arr, i, j);
                }
			}

            Exchange(arr, i+1, r);
            return i + 1;
        }

        private static void Exchange(int[] arr, int i, int j)
        {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }


    }
}
