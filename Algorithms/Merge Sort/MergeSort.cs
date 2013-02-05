using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _13_Merge_Sort
{
    //Write a program that sorts an array of integers using the merge sort algorithm
    class MergeSort
    {
        static void Main(string[] args)
        {
            int[] arr = new int[] { 1, 5, 3, 2, 4, 6, 7, 98, 4, 2 };
            arr = MergeSortReqursion(arr);

            for (int i = 0; i < arr.Length; i++)
            {
                Console.Write(arr[i] + " ");
            }

        }

        static int[] MergeSortReqursion(int[] arr)
        {
            if (arr.Length <= 1)
            {
                return arr;
            }

            int middle = arr.Length / 2;
            if (arr.Length % 2 > 0)
            {
                middle++;
            }
            int[] left = new int[middle];
            left = CopyArray(arr, 0, middle);
            int[] right = new int[middle];
            right = CopyArray(arr, left.Length, arr.Length);

            left = MergeSortReqursion(left);
            right = MergeSortReqursion(right);

            return Merge(left, right);
        }

        private static int[] Merge(int[] left, int[] right)
        {
            int[] arr = new int[left.Length + right.Length];
            
            int leftIndex = 0;
            int rightIndex = 0;

            for (int i = 0; i < left.Length + right.Length; i++)
            {
                if (leftIndex < left.Length && rightIndex < right.Length)
                {
                    if (left[leftIndex] <= right[rightIndex])
                    {
                        arr[i] = left[leftIndex];
                        leftIndex++;
                    }
                    else
                    {
                        arr[i] = right[rightIndex];
                        rightIndex++;
                    }
                }
                else if (leftIndex < left.Length)
                {
                    arr[i] = left[leftIndex];
                    leftIndex++;
                }
                else
                {
                    arr[i] = right[rightIndex];
                    rightIndex++;
                }
            }
            
            return arr;
        }

        private static int[] CopyArray(int[] arr, int from, int to)
        {
            int[] copyArr = new int[to - from];
            for (int i = 0; i < to - from; i++)
            {
                copyArr[i] = arr[i + from];
            }
            return copyArr;
        }
    }
}
