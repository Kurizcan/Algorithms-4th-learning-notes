import java.util.HashSet;
import java.util.Set;

public class Solution {
    // Parameters:
    //    numbers:     an array of integers
    //    length:      the length of array numbers
    //    duplication: (Output) the duplicated number in the array number,length of duplication array is 1,so using duplication[0] = ? in implementation;
    //                  Here duplication like pointor in C/C++, duplication[0] equal *duplication in C/C++
    //    这里要特别注意~返回任意重复的一个，赋值duplication[0]
    // Return value:       true if the input is valid, and there are some duplications in the array number
    //                     otherwise false

    // 法一：
    public boolean duplicate(int numbers[],int length,int [] duplication) {
        if(numbers == null || numbers.length <= 0)
            return false;
        Set<Integer> map = new HashSet<Integer>();
        for(int number: numbers) {
            if(map.contains(number)) {
                duplication[0] = number;
                return true;
            }
            else {
                map.add(number);
            }
        }
        return false;
    }

    // 法二：
    public boolean duplicate(int numbers[],int length,int [] duplication) {
        if (numbers == null)
            return false;
        Arrays.sort(numbers);
        for (int i = 0; i < length - 1; i++) {
            if (numbers[i] == numbers[i + 1]) {
                duplication[0] = numbers[i];
                return true;
            }
        }
        return false;
    }

    // 法三：   
    public boolean duplicate(int numbers[],int length,int [] duplication) {
        if (numbers == null)
            return false;
        for (int i = 0; i < length; i++) {
            while (numbers[i] != i) {
                int temp = numbers[i];
                if (numbers[i] == numbers[temp]) {
                    duplication[0] = temp;
                    return true; 
                }
                else {
                    numbers[i] = numbers[temp];
                    numbers[temp] = temp; 
                } 
            }
        }
        return false;
    }

    // 法四：不修改数组
}