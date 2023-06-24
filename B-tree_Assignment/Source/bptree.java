import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class bptree {
    public Node root = null;
    public static int deg;
    public static int count;
    public ArrayList<Pair> leaf = new ArrayList<>();
    public ArrayList<Node> save_node = new ArrayList<>();

    class Pair implements Comparable<Pair> {
        int value;
        int key;

        public int compareTo(Pair p) {

            int gap = p.key;

            return this.key - gap;
        }

        public Pair(Pair other) {
            this.value = other.value;
            this.key = other.key;
        }

        public Pair() {

        }

        public Pair(int a, int b) {
            value = b;
            key = a;
        }
    }

    class Node {
        int degree = deg;
        int m;
        boolean isleaf;
        Node[] p_child = new Node[degree + 1];
        Pair[] key_value = new Pair[degree];
        Node r;
        Node parent;

        public Node() {
            m = 0;
            for (int i = 0; i < degree; i++) {
                p_child[i] = null;
            }
            isleaf = false;
            r = null;
            parent = null;
            for (int i = 0; i < degree; i++) {
                key_value[i] = new Pair();
                key_value[i].key = Integer.MAX_VALUE;
            }
        }

        public Node(int x) {
            m = 0;
            r = null;
            parent = null;
            isleaf = false;
            for (int i = 0; i < degree; i++) {
                key_value[i] = new Pair();
                key_value[i].key = Integer.MAX_VALUE;
            }
            key_value[0].key = x;
        }

        public Node(Node other) {
            this.m = other.m;
            this.r = other.r;
            this.parent = other.parent;
            this.isleaf = other.isleaf;
            for (int i = 0; i < deg; i++) {
                this.key_value[i] = other.key_value[i];
            }
            for (int i = 0; i < deg + 1; i++) {
                this.p_child[i] = other.p_child[i];
            }

        }
    }

    private static int[] binarySearch(Pair[] A, int n) {
        int first = 0;
        int last = A.length - 1;
        int mid = 0;
        int[] temp = new int[2];
        while (first <= last) {
            mid = (first + last) / 2;
            temp[1] = mid;
            if (n == A[mid].key) {
                temp[0] = A[mid].key;
                return temp;
            } else {
                if (n < A[mid].key)
                    last = mid - 1;
                else
                    first = mid + 1;
            }
        }
        temp[0] = A[mid].key;
        return temp;
    }

    //find leaf
    public void insert(int x, int value) {
        Node temp = root;
        //root
        if (temp == null) {
            insertKey(temp, x, value);
        } else {
            while (temp.p_child[0] != null) {
                int[] key_value = binarySearch(temp.key_value, x);
                if (key_value[0] == x) {
                    System.out.println(x + " is duplited key");
                    this.root = Goto_root(temp);
                    return;
                } else if (key_value[0] < x) {
                    temp = temp.p_child[key_value[1] + 1];
                } else if (key_value[0] > x) {
                    temp = temp.p_child[key_value[1]];
                }
            }
            int[] key_value = binarySearch(temp.key_value, x);
            if (key_value[0] == x) {
                System.out.println(x + " is duplited key");
                this.root = Goto_root(temp);
            } else {
                this.root = insertKey(temp, x, value);
            }
        }
    }

    //insertkey is to insert the key
    public Node insertKey(Node root, int x, int value) {
        Node p = root;
        if (root == null) {
            this.root = new Node(x);
            this.root.isleaf = true;
            this.root.key_value[0].value = value;
            this.root.m++;
        } else if (p.p_child[0] == null) {
            p.m++;
            p.key_value[p.m - 1].key = x;
            p.key_value[p.m - 1].value = value;
            Arrays.sort(p.key_value);
            //in leaf node do split
            if (p.m >= p.degree) {
                p = leaf_split(p, x, value);
            }

            //the process to go root and split

            while (true) {
                if (p.parent == null) {
                    if (p.m >= p.degree) {
                        p = Non_leaf_split(p);
                    }
                    break;
                }
                if (p.m >= p.degree) {
                    p = Non_leaf_split(p);
                } else {
                    p = p.parent;
                }
            }
        }
        return p;
    }

    //Non leaf split
    public Node Non_leaf_split(Node left) {
        //if parent
        if (left.m >= left.degree && left.parent != null) {
            int temp = left.m / 2;
            int temp2 = (left.degree + 1) / 2;
            if (left.degree % 2 == 0) {
                temp2++;
            }
            Node newNode = new Node();
            newNode.parent = left.parent;
            left.parent.key_value[left.parent.m].key = left.key_value[temp].key;
            left.key_value[temp].key = Integer.MAX_VALUE;
            left.m = temp;
            left.isleaf = false;
            newNode.m = temp;
            newNode.isleaf = false;
            newNode.r = left.r;
            left.r = newNode;
            if (left.degree % 2 == 0) {
                newNode.m--;
            }
            for (int i = 0; i < (left.degree + 1) / 2; i++) {
                newNode.p_child[i] = left.p_child[temp2 + i];
                left.p_child[temp2 + i].parent = newNode;
                left.p_child[temp2 + i] = null;
            }
            for (int i = 0; i < newNode.m; i++) {
                newNode.key_value[i].key = left.key_value[temp + i + 1].key;
                left.key_value[temp + i + 1].key = Integer.MAX_VALUE;
            }
            left.parent.m++;
            Arrays.sort(left.parent.key_value);
            int index = 1;
            boolean isindex = false;
            for (int i = 0; i < deg; i++) {
                if (left.parent.p_child[i] == left) {
                    isindex = true;
                    index = i + 1;
                    break;
                }
            }
            Node[] tempNode = new Node[deg + 1];
            int j = 0;
            for (int i = index; i < deg; i++) {
                if (left.parent.p_child[i] == null) {
                    left.parent.p_child[i + 1] = null;
                    continue;
                }
                tempNode[j] = left.parent.p_child[i];
                j++;
            }
            for (int i = 0; i < j; i++) {
                left.parent.p_child[index + i + 1] = tempNode[i];
            }
            if (!isindex) {
                index = 1;
            }
            left.parent.p_child[index - 1] = left;
            left.parent.p_child[index] = newNode;
            left.key_value[left.m].value = -1;
            newNode.key_value[left.m].value = -1;
        }
        //if not leaf
        else if (left.m >= left.degree && left.parent == null) {
            int temp = left.m / 2;
            int temp2 = (left.degree + 1) / 2;
            if (left.degree % 2 == 0) {
                temp2++;
            }
            Node newNode = new Node();
            newNode.m = temp;
            if (left.degree % 2 == 0) {
                newNode.m--;
            }
            Node parent = new Node(left.key_value[temp].key);
            parent.m++;
            left.parent = parent;
            newNode.parent = left.parent;
            newNode.isleaf = false;
            left.isleaf = false;
            left.key_value[temp].key = Integer.MAX_VALUE;
            newNode.r = left.r;
            left.r = newNode;
            Arrays.sort(left.parent.key_value);
            for (int i = 0; i < (left.degree + 1) / 2; i++) {
                newNode.p_child[i] = left.p_child[temp2 + i];
                left.p_child[temp2 + i].parent = newNode;
                left.p_child[temp2 + i] = null;
            }
            for (int i = 0; i < newNode.m; i++) {
                newNode.key_value[i].key = left.key_value[temp + i + 1].key;
                left.key_value[temp + i + 1].key = Integer.MAX_VALUE;
            }

            int index = 1;
            boolean isindex = false;
            for (int i = 0; i < deg; i++) {
                if (left.parent.p_child[i] == left) {
                    isindex = true;
                    index = i + 1;
                    break;
                }
            }

            left.m = temp;
            Node[] tempNode = new Node[deg + 1];
            int j = 0;
            for (int i = index; i < deg; i++) {
                if (left.parent.p_child[i] == null) {
                    left.parent.p_child[i + 1] = null;
                    continue;
                }
                tempNode[j] = left.parent.p_child[i];
                j++;
            }
            for (int i = 0; i < j; i++) {
                left.parent.p_child[index + i + 1] = tempNode[i];
            }
            if (!isindex) {
                index = 1;
            }
            left.parent.p_child[index - 1] = left;
            left.parent.p_child[index] = newNode;

        }

        return left.parent;
    }

    public Node leaf_split(Node left, int x, int value) {
        //if parent
        if (left.m >= left.degree && left.parent != null) {
            int temp = left.m / 2;
            Node newNode = new Node();
            newNode.parent = left.parent;
            newNode.m = left.m - temp;
            for (int i = 0; i < newNode.m; i++) {
                newNode.key_value[i].key = left.key_value[temp + i].key;
                newNode.key_value[i].value = left.key_value[temp + i].value;
                left.key_value[temp + i].key = Integer.MAX_VALUE;
                left.key_value[temp + i].value = Integer.MAX_VALUE;
            }
            newNode.isleaf = true;
            left.isleaf = true;
            left.m = temp;
            newNode.r = left.r;
            left.r = newNode;
            left.parent.key_value[left.parent.m].key = left.r.key_value[0].key;
            left.parent.m++;
            Arrays.sort(left.parent.key_value);
            int index = 1;
            for (int i = 0; i < deg; i++) {
                if (left.parent.p_child[i] == left) {
                    index = i + 1;
                    break;
                }
            }
            Node[] tempNode = new Node[deg + 1];
            int j = 0;
            for (int i = index; i < deg + 1; i++) {
                if (left.parent.p_child[i] == null) {
                    break;
                }
                tempNode[j] = left.parent.p_child[i];
                j++;
            }
            for (int i = 0; i < j; i++) {
                left.parent.p_child[index + i + 1] = tempNode[i];
            }
            left.parent.p_child[index - 1] = left;
            left.parent.p_child[index] = newNode;


        }
        //if no parent
        else if (left.m >= left.degree && left.parent == null) {
            int temp = left.m / 2;
            Node newNode = new Node();
            Node Node = new Node(x);
            left.parent = Node;
            newNode.m = left.m - temp;
            newNode.parent = left.parent;
            for (int i = 0; i < newNode.m; i++) {
                newNode.key_value[i].key = left.key_value[temp + i].key;
                newNode.key_value[i].value = left.key_value[temp + i].value;
                left.key_value[temp + i].key = Integer.MAX_VALUE;
                left.key_value[temp + i].value = Integer.MAX_VALUE;
            }

            newNode.r = left.r;
            left.r = newNode;
            newNode.isleaf = true;
            left.isleaf = true;

            left.m = temp;
            left.parent.key_value[left.parent.m].key = left.r.key_value[0].key;
            int index = 1;
            for (int i = 0; i < deg; i++) {
                if (left.parent.p_child[i] == left) {
                    index = i + 1;
                    break;
                }
            }
            left.parent.m++;
            Node[] tempNode = new Node[deg + 1];
            int j = 0;
            for (int i = index; i < deg; i++) {
                if (left.parent.p_child[i] == null) {
                    left.parent.p_child[i + 1] = null;
                    continue;
                }
                tempNode[j] = left.parent.p_child[i];
                j++;
            }
            for (int i = 0; i < j; i++) {
                left.parent.p_child[index + i + 1] = tempNode[i];
            }
            left.parent.p_child[index - 1] = left;
            left.parent.p_child[index] = newNode;
            Arrays.sort(left.parent.key_value);
        }
        return left.parent;
    }

    public Node Goto_root(Node p) {
        while (true) {
            if (p.parent == null) {
                break;
            } else {
                p = p.parent;
            }
        }
        return p;
    }

    public Node Find_left_node(Node s_temp, int p_to_child) {
        if (p_to_child - 1 < 0) {
            return null;
        }
        s_temp = s_temp.parent.p_child[p_to_child - 1];
        return s_temp;
    }

    public Node Find_right_node(Node s_temp, int p_to_child) {
        if (p_to_child + 1 > deg || s_temp.parent.p_child[p_to_child + 1] == null) {
            return null;
        }
        s_temp = s_temp.parent.p_child[p_to_child + 1];
        return s_temp;
    }


    //find leaf delete
    public void delete(int x) {
        Node temp = root;
        Node r_temp = null;
        int index = -1;
        int p_to_child = -1;
        //root
        if(temp == null) {
            System.out.println("None");
        }

        else{
            while(temp.p_child[0] != null){
                int[] key_value = binarySearch(temp.key_value,x);

                if(key_value[0] < x) {
                    temp = temp.p_child[key_value[1]+1];
                    p_to_child = key_value[1] +1;
                }
                else if(key_value[0] == x){
                    r_temp = temp;
                    index = key_value[1];
                    temp = temp.p_child[key_value[1] + 1];
                    p_to_child = key_value[1] + 1;
                }
                else if(key_value[0] > x) {
                    temp = temp.p_child[key_value[1]];
                    p_to_child = key_value[1];
                }
            }
        }

        int[] find_k = binarySearch(temp.key_value,x);
        if(p_to_child == -1){
            this.root = temp;
        }

        if(find_k[0] != x){
            System.out.println("NOT FOUND");
            this.root = Goto_root(temp);
        }

        if(temp.parent == null){
            temp.key_value[find_k[1]].key = Integer.MAX_VALUE;
            temp.m--;
            Arrays.sort(temp.key_value);
            this.root = temp;
        }

        else{
            temp.m--;
            temp.key_value[find_k[1]].key = Integer.MAX_VALUE;
            Arrays.sort(temp.key_value);
            //leaf node end
            if (temp.m >= (deg - 1) / 2) {
                if(r_temp != null){
                    r_temp.key_value[index].key = temp.key_value[find_k[1]].key;
                }
                this.root = Goto_root(temp);
            } else {
                Node left_node = Find_left_node(temp,p_to_child);
                Node right_node = Find_right_node(temp,p_to_child);

                //left sibling
                if(left_node != null && left_node.m-1 >= (deg-1)/2){
                    temp.key_value[temp.m].key = left_node.key_value[left_node.m-1].key;
                    temp.key_value[temp.m].value = left_node.key_value[left_node.m-1].value;
                    temp.m++;
                    left_node.key_value[left_node.m-1].key = Integer.MAX_VALUE;
                    left_node.m--;
                    Arrays.sort(temp.key_value);


                    temp.parent.key_value[p_to_child-1].key = temp.key_value[0].key;
                    if(r_temp != null && r_temp != temp.parent){
                        r_temp.key_value[index].key= temp.key_value[0].key;
                    }
                    this.root = Goto_root(temp);
                }
                //rigft sibling
                else if(right_node != null && right_node.m-1 >= (deg -1) /2 ){
                    temp.key_value[temp.m].key = right_node.key_value[0].key;
                    temp.key_value[temp.m].value = right_node.key_value[0].value;
                    temp.m++;
                    right_node.key_value[0].key = Integer.MAX_VALUE;
                    right_node.m--;
                    Arrays.sort(right_node.key_value);
                    if(r_temp != null){
                        r_temp.key_value[index].key = temp.key_value[0].key;
                    }
                    temp.parent.key_value[p_to_child].key = right_node.key_value[0].key;
                    this.root = Goto_root(temp);
                }
                //merge
                else{
                    if(right_node != null){

                        for(int i = 0; i<right_node.m;i++){
                            temp.key_value[temp.m] = new Pair(right_node.key_value[i]);
                            temp.m++;
                        }
                        temp.parent.p_child[p_to_child].r = temp.parent.p_child[p_to_child+1].r;
                        temp.parent.p_child[p_to_child+1] = null;

                        for(int i = p_to_child+1; i<deg;i++){
                            if(temp.parent.p_child[i+1] == null){
                                temp.parent.p_child[i] = null;
                                continue;
                            }
                            temp.parent.p_child[i] = new Node(temp.parent.p_child[i+1]);
                            temp.parent.p_child[i-1].r = temp.parent.p_child[i];
                        }
                        if(temp.parent.p_child[deg] != null){
                            temp.parent.p_child[deg-1].r = temp.parent.p_child[deg].r;
                        }

                        temp.parent.p_child[deg] =null;

                        temp.parent.key_value[p_to_child].key = Integer.MAX_VALUE;
                        temp.parent.m--;
                        Arrays.sort(temp.parent.key_value);
                        if(r_temp != null){
                            r_temp.key_value[index].key = temp.key_value[0].key;
                        }

                    }
                    else{
                        for(int i = 0;i<temp.m;i++){
                            left_node.key_value[left_node.m] = new Pair(temp.key_value[i]);
                            left_node.m++;
                        }
                        left_node.parent.p_child[p_to_child-1].r = left_node.parent.p_child[p_to_child].r;
                        left_node.parent.p_child[p_to_child] = null;

                        for(int i = p_to_child;i<deg;i++){
                            if(left_node.parent.p_child[i+1] == null){
                                left_node.parent.p_child[i] = null;
                                continue;
                            }
                            left_node.parent.p_child[i] = new Node(left_node.parent.p_child[i+1]);
                            left_node.parent.p_child[i-1].r = left_node.parent.p_child[i];
                        }
                        if(left_node.parent.p_child[deg] != null){
                            left_node.parent.p_child[deg-1].r = left_node.parent.p_child[deg].r;
                        }
                        left_node.parent.p_child[deg] = null;

                        left_node.parent.key_value[p_to_child-1].key = Integer.MAX_VALUE;
                        left_node.parent.m--;
                        Arrays.sort(temp.parent.key_value);
                        if(r_temp != null){
                            r_temp.key_value[index].key= temp.key_value[0].key;
                        }

                    }
                    temp = temp.parent;
                    //non leaf mergee
                    while(true){
                        if(temp.parent == null){
                            if(temp.m == 0){
                                this.root = temp.p_child[0];
                                this.root.parent =null;
                            }
                            break;
                        }
                        if(temp.m >= (deg-1)/2){
                            this.root = Goto_root(temp);
                            break;
                        }

                        else{
                            temp = parent_merge(temp);
                        }

                        temp = temp.parent;
                    }
                }
            }
        }
    }

    public Node parent_merge(Node temp) {
        int to_child = -1;
        for (int i = 0; i < temp.parent.m + 1; i++) {
            if (temp.parent.p_child[i] == temp) {
                to_child = i;
            }
        }

        Node left_sibling = null;
        Node right_sibling = null;

        left_sibling = Find_left_node(temp, to_child);
        right_sibling = Find_right_node(temp, to_child);
        int child_index = -1;

        if (left_sibling != null) {

            temp.key_value[temp.m].key = temp.parent.key_value[to_child - 1].key;
            temp.parent.key_value[to_child - 1].key = Integer.MAX_VALUE;
            temp.parent.m--;
            temp.m++;
            Arrays.sort(temp.key_value);
            Arrays.sort(temp.parent.key_value);
            if (left_sibling.m + temp.m >= deg) {

                temp.parent.key_value[temp.parent.m].key = left_sibling.key_value[left_sibling.m-1].key;
                left_sibling.key_value[left_sibling.m].key = Integer.MAX_VALUE;
                left_sibling.m--;
                Node[] tempNode = new Node[deg+1];
                for(int i = 0;i <deg;i++){
                    if(temp.p_child[i] != null){
                        tempNode[i] = new Node(temp.p_child[i]);
                    }
                }
                for(int i = 0; i<deg;i++){
                    temp.p_child[i+1] = tempNode[i];
                    if(temp.p_child[i+1] != null){
                        for(int j = 0 ;j<deg;j++){
                            if(temp.p_child[i+1].p_child[j] != null){
                                temp.p_child[i+1].p_child[j].parent = temp.p_child[i+1];
                            }
                        }
                    }
                }
                temp.p_child[0] = new Node(left_sibling.p_child[left_sibling.m+1]);
                temp.p_child[0].r = temp.p_child[1];
                for(int i = 0 ; i<deg;i++){
                    if(temp.p_child[0].p_child[i] != null){
                        temp.p_child[0].p_child[i].parent = temp.p_child[0];
                    }
                }
                temp.p_child[0].parent = temp;
                left_sibling.p_child[left_sibling.m+1] = null;
                left_sibling.p_child[left_sibling.m].r = temp.p_child[0];
                temp.parent.m++;
                Arrays.sort(temp.parent.key_value);

            }
            else {


                temp.parent.p_child[to_child] = null;
                for (int i = to_child; i < deg; i++) {
                    if(left_sibling.parent.p_child[i+1] == null){
                        left_sibling.parent.p_child[i] = null;
                        continue;
                    }
                    left_sibling.parent.p_child[i] = new Node(left_sibling.parent.p_child[i + 1]);
                    for(int j = 0; j<deg;j++){
                        if(left_sibling.parent.p_child[i].p_child[j] != null){
                            left_sibling.parent.p_child[i].p_child[j].parent = left_sibling.parent.p_child[i];
                        }
                    }
                }
                left_sibling.parent.p_child[deg]=null;

                for (int i = 0; i < deg; i++) {
                    if (left_sibling.p_child[i] == null) {
                        child_index = i;
                        break;
                    }
                }

                for (int i = 0; i < temp.m; i++) {
                    left_sibling.key_value[left_sibling.m].key = temp.key_value[i].key;
                    left_sibling.m++;
                }

                for (int i = 0; i < deg; i++) {
                    if (temp.p_child[i] != null) {
                        left_sibling.p_child[child_index] = new Node(temp.p_child[i]);
                        left_sibling.p_child[child_index-1].r = left_sibling.p_child[child_index];
                        for(int j = 0; j<deg;j++){
                            if(left_sibling.p_child[child_index].p_child[j] != null){
                                left_sibling.p_child[child_index].p_child[j].parent = left_sibling.p_child[child_index];
                            }
                        }
                        left_sibling.p_child[child_index].parent = left_sibling;
                        temp.p_child[i] = null;
                        child_index++;
                    } else {

                        temp.m = 0;
                        break;
                    }
                }

            }
            return left_sibling;
        }

        else {

            temp.key_value[temp.m].key = temp.parent.key_value[to_child].key;
            temp.parent.key_value[to_child].key = Integer.MAX_VALUE;


            temp.parent.m--;
            temp.m++;
            Arrays.sort(temp.key_value);
            Arrays.sort(temp.parent.key_value);

            if (temp.m + right_sibling.m >= deg) {

                temp.parent.key_value[temp.parent.m].key = right_sibling.key_value[0].key;

                right_sibling.key_value[0].key = Integer.MAX_VALUE;
                Arrays.sort(right_sibling.key_value);

                temp.parent.m++;
                right_sibling.m--;
                temp.p_child[temp.m] = new Node(right_sibling.p_child[0]);
                for(int i = 0; i<deg; i++){
                    if(temp.p_child[temp.m].p_child[i] != null) {
                        temp.p_child[temp.m].p_child[i].parent = temp.p_child[temp.m];
                    }
                }
                temp.p_child[temp.m].parent = temp;
                temp.p_child[temp.m-1].r = temp.p_child[temp.m];
                right_sibling.p_child[0] = null;
                for (int i = 0; i < deg - 1; i++) {
                    if(right_sibling.p_child[i+1] == null){
                        right_sibling.p_child[i] = null;
                        continue;
                    }
                    right_sibling.p_child[i] = new Node(right_sibling.p_child[i + 1]);
                    for(int j = 0; j<deg;j++){
                        if(right_sibling.p_child[i].p_child[j] != null){
                            right_sibling.p_child[i].p_child[j].parent = right_sibling.p_child[i];
                        }
                    }
                }
                Arrays.sort(right_sibling.parent.key_value);
                right_sibling.p_child[deg] = null;

            }

            else {

                temp.parent.p_child[to_child+1] = null;

                for(int i = to_child+1; i<deg;i++){
                    if(temp.parent.p_child[i+1] == null){
                        temp.parent.p_child[i] = null;
                        continue;
                    }
                    temp.parent.p_child[i] = new Node(temp.parent.p_child[i+1]);
                    for(int j = 0 ; j<deg; j++){
                        if(temp.parent.p_child[i].p_child[j] != null){
                            temp.parent.p_child[i].p_child[j].parent = temp.parent.p_child[i];
                        }
                    }
                }
                for(int i = 0 ; i<deg;i++){
                    if(temp.p_child[i] == null){
                        child_index = i;
                        break;
                    }
                }

                for(int i = 0; i<right_sibling.m;i++){
                    temp.key_value[temp.m].key = right_sibling.key_value[i].key;
                    temp.m++;
                }

                for (int i = 0; i < deg; i++) {
                    if (right_sibling.p_child[i] != null) {
                        temp.p_child[child_index] = new Node(right_sibling.p_child[i]);
                        temp.p_child[child_index-1].r=temp.p_child[child_index];
                        temp.p_child[child_index].parent = temp;
                        for(int j = 0; j<deg;j++){
                            if(temp.p_child[child_index].p_child[i] != null){
                                temp.p_child[child_index].p_child[i].parent = temp.p_child[child_index];
                            }
                        }
                        right_sibling.p_child[i] = null;
                        child_index++;
                    } else {
                        right_sibling.m = 0;
                        break;
                    }
                }
            }
            return temp;
        }

    }

    //single_key_search
    public void single_key_search(int f_key) {
        Node f_tree = root;
        String a = "";
        if (f_tree == null) {
            System.out.println("Error");
        } else {

            while (f_tree.p_child[0] != null) {
                int[] key_value = binarySearch(f_tree.key_value, f_key);
                for (int i = 0; i < f_tree.m; i++) {
                    a += Integer.toString(f_tree.key_value[i].key);
                    if (i == f_tree.m - 1) {
                        break;
                    } else {
                        a += ", ";
                    }
                }
                if (key_value[0] <= f_key) {
                    f_tree = f_tree.p_child[key_value[1] + 1];
                } else if (key_value[0] > f_key) {
                    f_tree = f_tree.p_child[key_value[1]];
                }
                a += "\n";
            }

            int[] key_value = binarySearch(f_tree.key_value, f_key);
            if (f_tree.key_value[key_value[1]].key == f_key) {
                System.out.print(a);
                System.out.println(f_tree.key_value[key_value[1]].value);

            } else {

                System.out.println("NOT FOUND");
            }

        }
    }

    public void range_key_search(int start_key, int end_key) {
        Node r_tree = root;
        boolean isprint = false;
        if (r_tree == null) {
            System.out.println("Error");
        } else {
            while (r_tree.p_child[0] != null) {
                int[] key_value = binarySearch(r_tree.key_value, start_key);
                if (key_value[0] <= start_key) {
                    r_tree = r_tree.p_child[key_value[1] + 1];
                } else if (key_value[0] > start_key) {
                    r_tree = r_tree.p_child[key_value[1]];
                }
            }
        }
        while (true) {
            for (int i = 0; i < r_tree.m; i++) {
                if (r_tree.key_value[i].key <= end_key && r_tree.key_value[i].key >= start_key) {
                    isprint = true;
                    System.out.println(r_tree.key_value[i].key + ", " + r_tree.key_value[i].value);
                }

            }
            if (r_tree.r == null) {
                break;
            }
            r_tree = r_tree.r;
        }

        if (!isprint) {
            System.out.println("NOT FOUND");
        }
    }


    public static void create(String index_file) {

        try {

            FileWriter w = new FileWriter(index_file, false);
            w.write(Integer.toString(deg));
            w.flush();
            w.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void save(FileWriter w, Node now, int parent) {
        try {
            count++;
            int n = count;
            if (now.p_child[0] != null) {
                w.write("$ " + parent + " " + n + " / " + now.m + " / ");

                for (int i = 0; i < now.m; i++)
                    w.write(now.key_value[i].key + " ");

                w.write("\n");

                for (int i = 0; i < deg; i++) {

                    if (now.p_child[i] != null)
                        save(w, now.p_child[i], n);

                }
            }
            if (now.p_child[0] == null) {

                w.write("$ " + parent + " " + n + " / " + now.m + " / ");

                for (int i = 0; i < now.m; i++)
                    w.write(now.key_value[i].key + " ");

                w.write("\n");
                leaf.add(new Pair(n, parent));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save_layout(String index_file) {
        try {

            FileWriter w = new FileWriter(index_file, false);
            Node p = this.root;
            w.write(deg + "\n");
            save(w, root, 0);

            w.write("@ ");
            while (true) {
                if (p.p_child[0] == null) {
                    break;
                }
                p = p.p_child[0];
            }
            int j = 0;
            while (true) {
                if (p.r == null) {
                    w.write(leaf.get(j).key + " " + "-1" + " / " + p.m + " / ");
                    for (int i = 0; i < p.m; i++) {
                        w.write(p.key_value[i].value + " ");
                    }
                    break;
                }
                w.write(leaf.get(j).key + " " + leaf.get(j + 1).key + " / " + p.m + " / ");
                for (int i = 0; i < p.m; i++) {
                    w.write(p.key_value[i].value + " ");
                }
                w.write("@ ");
                p = p.r;
                j++;
            }
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void make_tree(String index_file) {

        File readfile = new File(index_file);

        String line = null;
        boolean flag = true;
        try {

            BufferedReader reader = new BufferedReader(new FileReader(readfile));
            save_node.add(0, null);
            while ((line = reader.readLine()) != null) {

                if (flag) {

                    deg = Integer.parseInt(line);
                    flag = false;

                } else {

                    if (line.substring(0, 1).equals("$")) {

                        String[] data = line.substring(2).split(" / ");

                        int parent_index = Integer.parseInt(data[0].split(" ")[0]);
                        int now_index = Integer.parseInt(data[0].split(" ")[1]);
                        int now_m = Integer.parseInt(data[1].split(" ")[0]);
                        Node new_node = new Node();
                        new_node.m = now_m;
                        // add pairs to node
                        for (int i = 0; i < new_node.m; i++) {

                            int key = Integer.parseInt(data[2].split(" ")[i]);
                            new_node.key_value[i].key = key;

                        }

                        // connect parent
                        if (parent_index == 0) {
                            save_node.add(now_index, new_node);
                            this.root = new_node;
                            root.parent = null;

                        } else {

                            save_node.add(now_index, new_node);
                            new_node.parent = save_node.get(parent_index);

                            for (int i = 0; i < deg; i++) {

                                if (new_node.parent.p_child[i] == null) {

                                    new_node.parent.p_child[i] = new_node;
                                    break;

                                }

                            }

                        }
                    } else { // leaf

                        String[] tmp = line.split("@");
                        for (int i = 1; i < tmp.length; i++) {

                            String[] data = tmp[i].substring(1).split(" / ");
                            int now_index = Integer.parseInt(data[0].split(" ")[0]);
                            int r_index = Integer.parseInt(data[0].split(" ")[1]);
                            int now_m = Integer.parseInt(data[1].split(" ")[0]);

                            if(r_index == -1){
                                save_node.get(now_index).r = null;
                            }
                            else {
                                save_node.get(now_index).r = save_node.get(r_index);
                            }
                            for (int j = 0; j < now_m; j++) {
                                int value = Integer.parseInt(data[2].split(" ")[j]);
                                save_node.get(now_index).key_value[j].value = value;
                            }

                        }

                        break;

                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read_data(String fileName, String act) {

        List<Integer> arr = new ArrayList<>();
        File readfile = new File(fileName);

        String line = null;

        try {

            BufferedReader reader = new BufferedReader(new FileReader(readfile));

            while ((line = reader.readLine()) != null) {

                if (act.equals("-i")) {

                    String[] tmp = line.split(",");
                    int key = Integer.parseInt(tmp[0]);
                    int val = Integer.parseInt(tmp[1]);

                    insert(key, val);

                } else
                    delete(Integer.parseInt(line));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        bptree tree = new bptree();
        String act = args[0];
        String index_file = args[1];

        switch (act) {

            case "-c": // create

                deg = Integer.parseInt(args[2]); // save deg
                create(index_file);
                break;

            case "-i":
                String in_data = args[2];

                tree.make_tree(index_file);

                tree.read_data(in_data, act);

                break;

            case "-d": // delete

                String de_data = args[2];

                tree.make_tree(index_file);
                tree.read_data(de_data, act);

                break;

            case "-s": // single key search

                int find_key = Integer.parseInt(args[2]);

                tree.make_tree(index_file);
                tree.single_key_search(find_key);
                break;

            case "-r": // ranged search

                int from_key = Integer.parseInt(args[2]);
                int to_key = Integer.parseInt(args[3]);

                tree.make_tree(index_file);
                tree.range_key_search(from_key, to_key);
                break;

        }
        if (act.equals("-i") || act.equals("-d"))

            tree.save_layout(index_file);
    }
}