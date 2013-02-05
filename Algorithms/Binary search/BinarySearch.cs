using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _11_Binary_search
{
    //Write a program that finds the index of given element in a 
    //sorted array of integers by using the binary search algorithm
    class BinarySearch
    {
        static void Main(string[] args)
        {
            int[] arr = new int[]{1,2,3,4,5,6,7,8,9};
            int key = 9;
            int len = arr.Length;

            if (len == 0)
            {
                Console.WriteLine("array is empty");
                return;
            }
            else if (len == 1)
            {
                if (arr[0] == key)
                    Console.WriteLine("index: 0");
                else
                    Console.WriteLine("no element found");
                return;
            }
            else if (len == 2)
            {
                if (arr[0] == key)
                    Console.WriteLine("index: 0");
                else if (arr[1] == key)
                    Console.WriteLine("index: 1");
                else
                    Console.WriteLine("no element found");
                return;
            }


            Console.WriteLine("Recursion:");
            int index = BinarySearchReqursion(arr, arr.Length, 0, key);
            if (index == -1)
            {
                Console.WriteLine("no element found");
            }
            else
            {
                Console.WriteLine("index: " + index);
            }

            Console.WriteLine();
            Console.WriteLine("Iteration:");
            index = BinarySearchIterative(arr, arr.Length, 0, key);
            if (index == -1)
            {
                Console.WriteLine("no element found");
            }
            else
            {
                Console.WriteLine("index: " + index);
            }
        }

        public static int BinarySearchReqursion(int[] arr, int high, int low, int key)
        {
            if (high <= low)
            {
                return -1;
            }

            int middle = low + (high - low) / 2;

            if (key == arr[middle])
            {
                return middle;
            }

            if (key > arr[middle])
            {
                middle = BinarySearchReqursion(arr, high, middle+1, key);
            }
            else
            {
                middle = BinarySearchReqursion(arr, middle-1, low, key);
            }
            return middle;
        }

        public static int BinarySearchIterative(int[] arr, int high, int low, int key)
        {
            int middle;
            while (high > low)
            {
                middle = low + (high - low) / 2;
                if (arr[middle] > key)
                {
                    return BinarySearchIterative(arr, middle - 1, low, key);
                }
                else if (arr[middle] < key)
                {
                    return BinarySearchIterative(arr, high, middle + 1, key);
                }
                else
                {
                    return middle;
                }
            }
            return -1;
        }
    }
}
