using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _01_NxN_matrix_print
{
    class MatrixPrint
    {
        static void Main(string[] args)
        {
            int n = int.Parse(Console.ReadLine());
            int[,] matrix = new int[n, n];

            int i = 1;
            for (int col = 0; col < matrix.GetLength(1); col++)
            {
                for (int row = 0; row < matrix.GetLength(0); row++)
                {
                    matrix[row, col] = i;
                    i++;
                }
            }

            PrintMatrix(matrix);

            i = 1;
            bool inverse = false;
            for (int col = 0; col < matrix.GetLength(1); col++)
            {
                if (inverse)
                {
                    for (int row = matrix.GetLength(0) - 1; row >= 0; row--)
                    {
                        matrix[row, col] = i;
                        i++;
                    }
                }
                else
                {
                    for (int row = 0; row < matrix.GetLength(0); row++)
                    {
                        matrix[row, col] = i;
                        i++;
                    }
                }
                inverse = !inverse;
            }

            PrintMatrix(matrix);


            // C
            i = 1;
            int col_;
            for (int index = matrix.GetLength(0)-1; index >= 0; index--)
            {
                col_ = 0;
                for (int row = index; row < matrix.GetLength(0); row++)
                {
                    matrix[row, col_] = i;
                    i++;
                    col_++;
                }
            }
            int row_;
            for (int index = 1; index < matrix.GetLength(1); index++)
            {
                row_ = 0;
                for (int col = index; col < matrix.GetLength(0); col++)
                {
                    matrix[row_, col] = i;
                    i++;
                    row_++;
                }
            }


            PrintMatrix(matrix);


            //D
            int m = 1;
            n = matrix.GetLength(0) - 1;
            for (int k = 0; k < n - 1; k++)
            {
                for (i = k; i < n - k; i++)
                {
                    matrix[i, k] = m;
                    m++;
                }

                for (i = k; i < n - k; i++)
                {
                    matrix[n - k, i] = m;
                    m++;
                }

                for (i = n - k; i > k; i--)
                {
                    matrix[i, n - k] = m;
                    m++;
                }

                for (i = n - k; i > k; i--)
                {
                    matrix[k, i] = m;
                    m++;
                }
                 
            }

            if (matrix.GetLength(0) % 2 > 0)
            {
                n = matrix.GetLength(0) / 2;
                matrix[n, n] = m;
            }

            PrintMatrix(matrix);

        }


        static void PrintMatrix(int[,] matrix)
        {
            StringBuilder str = new StringBuilder();
            for (int row = 0; row < matrix.GetLength(0); row++)
            {
                str.Clear();
                for (int col = 0; col < matrix.GetLength(1); col++)
                {
                    str.Append(String.Format("{0,4} ",matrix[row, col]));
                }
                Console.WriteLine(str.ToString());
            }
            Console.WriteLine();
        }
    }
}
