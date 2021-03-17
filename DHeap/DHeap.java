/**
 * D-Heap
 */

public class DHeap
{

    private int min, size, max_size, d;
    private DHeap_Item[] array;

	// Constructor
	// m_d >= 2, m_size > 0
    DHeap(int m_d, int m_size) {
               max_size = m_size;
			   d = m_d;
               array = new DHeap_Item[max_size];
               size = 0;
               min = 0;
    }
	
	/**
	 * public int getSize()
	 * Returns the number of elements in the heap.
	 */
	public int getSize() {
		return size;
	}
	
  /**
     * public int arrayToHeap()
     *
     * The function builds a new heap from the given array.
     * Previous data of the heap should be erased.
     * preconidtion: array1.length() <= max_size
     * postcondition: isHeap()
     * 				  size = array.length()
     * Returns number of comparisons along the function run. 
	 */
    public int arrayToHeap(DHeap_Item[] array1) 
    {
        size = array1.length;
        for(int i = 0; i<size; i++) {
        	array[i] = array1[i];
        }
        
        int numOfComparisons = 0;
        for(int i = array[size-1].getPos(); i >= 0; i--){
        	if(hasChild(array, array[i])){
        		numOfComparisons += heapify_down(array, array[i]);
        	}
        }
        return numOfComparisons;        
    }
    //return true if and only if the item has child
    private boolean hasChild(DHeap_Item[] heapArray, DHeap_Item item){
    	int pos = item.getPos();
    	int childPos = child(pos, 1, d);
    	
    	// added to check if childPos is not out of boundry
    	return (childPos<size)&&heapArray[childPos] != null;
    }

    /**
     * public boolean isHeap()
     *
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property or has size == 0.
     *   
     */
    public boolean isHeap() {   	
    	
    	// added terminal case:
    	if(size == 0) {
    		return true;
    	}
    	
    	for(DHeap_Item item : this.array){
    		if(hasChild(array, item)){
    			for(int k = 1 ; k <= d; k++){
    				// added to check if child isnt out of bounds
    				int childPos = child(item.getPos(), k, this.d);
    				if(childPos < size && array[childPos] != null){
    					if(item.getKey() > array[childPos].getKey()){
    						return false;
    					}
    				}
    			}
    		}
    	}
    	return true;
    }


 /**
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     *
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     *
     * The methods compute the index of the parent and the k-th child of 
     * vertex i in a complete D-ary tree stored in an array. 
     * Note that indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) { return i == 0? 0: (i-1)/d;}
    	
    public static int child (int i, int k, int d) { return i*d + k;  }  

    /**
    * public int Insert(DHeap_Item item)
    *
	* Inserts the given item to the heap.
	* Returns number of comparisons during the insertion.
	*
    * precondition: item != null
    *               isHeap()
    *               size < max_size
    * 
    * postcondition: isHeap()
    */
    public int Insert(DHeap_Item item) 
    {        
    	this.array[size] = item;    	
    	item.setPos(size);   	
    	size += 1;
    	int numOfComparisons =  heapify_up(array, item);
    	return numOfComparisons;
    }
    //returns the number of comparisons after the heapifying up
    private int heapify_up(DHeap_Item[] heapArray, DHeap_Item item){
    	int parentPos = parent(item.getPos(), d);
    	if(heapArray[parentPos].getKey() <= item.getKey()){
    		return 1;    		  		
    	}
    	swapWithParent(heapArray, item);
    	return 1 + heapify_up(heapArray, heapArray[parentPos]);    
    }
    
    //swapping with parent of current item
    private void swapWithParent(DHeap_Item[] heapArray, DHeap_Item item){
    	
    	// save original positions
    	int parentPos = parent(item.getPos(), d);
    	int itemPos = item.getPos();    	    	
    	DHeap_Item parent = heapArray[parentPos];
    	heapArray[parentPos] = item;
    	heapArray[itemPos] = parent;
    	
    	// swap positions
    	parent.setPos(itemPos);
    	item.setPos(parentPos);
    }

 /**
    * public int Delete_Min()
    *
	* Deletes the minimum item in the heap.
	* Returns the number of comparisons made during the deletion.
    * 
	* precondition: size > 0
    *               isHeap()
    * 
    * postcondition: isHeap()
    */
    public int Delete_Min()
    {
    	DHeap_Item item = array[size-1];
     	array[size-1] = null;
     	size--;
     	array[0] = item;
     	
     	//change item.Pos
     	item.setPos(0);
     	
     	return heapify_down(array, item);
    }
    
    //returns the number of comparisons after the heapifying down
    private int heapify_down(DHeap_Item[] heapArray, DHeap_Item item){
    	int minChildPos = minChild_numOfComps(heapArray, item)[0];
    	int num = minChild_numOfComps(heapArray, item)[1];
    	if((minChildPos >= size)|| (item.getKey() <= heapArray[minChildPos].getKey())){return 1;}
    	swapWithParent(heapArray, heapArray[minChildPos]);
    	return 1 + heapify_down(heapArray, heapArray[minChildPos])+num;
    }
    
    //returns an array contains the position of minimum child of the item & the number of comp's
    private int[] minChild_numOfComps(DHeap_Item[] heapArray, DHeap_Item item){
    	int numOfComparisons = 0;
    	int minChild = child(item.getPos(), 1, d);
    	int k = 2;
    	int ind = child(item.getPos(), k, d);
    	while ((k <= d) && (ind < size) && (heapArray[ind] != null)){ 
            if (heapArray[ind].getKey() < heapArray[minChild].getKey()){ 
                minChild = ind;
            }
            k++;
            numOfComparisons++;
            ind = child(item.getPos(), k, d);
    	}
    	int[] array = new int[2];
    	array[0] = minChild;
    	array[1] = numOfComparisons;
    	return array;
    }


    /**
     * public DHeap_Item Get_Min()
     *
	 * Returns the minimum item in the heap.
	 *
     * precondition: heapsize > 0
     *               isHeap()
     *		size > 0
     * 
     * postcondition: isHeap()
     */
    public DHeap_Item Get_Min(){return array[0];}
	
  /**
     * public int Decrease_Key(DHeap_Item item, int delta)
     *
	 * Decerases the key of the given item by delta.
	 * Returns number of comparisons made as a result of the decrease.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Decrease_Key(DHeap_Item item, int delta){

    	int key = item.getKey();
    	item.setKey(key-delta);
    	int numOfComparisons =  heapify_up(array, item);
    	return numOfComparisons;
    }
	
	  /**
     * public int Delete(DHeap_Item item)
     *
	 * Deletes the given item from the heap.
	 * Returns number of comparisons during the deletion.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete(DHeap_Item item){
    	int delta = item.getKey()-min+2; 
    	int op1 =  Decrease_Key(item, delta);
    	int op2 = Delete_Min();
    	return op1 + op2;
    }
	
	/**
	* Sort the input array using heap-sort (build a heap, and 
	* perform n times: get-min, del-min).
	* Sorting should be done using the DHeap, name of the items is irrelevant.
	* 
	* Returns the number of comparisons performed.
	* 
	* postcondition: array1 is sorted 
	*/
	public static int DHeapSort(int[] array1, int d) {
		int numOfComparisons = 0;
		
		DHeap heap = new DHeap(d, array1.length);
		heap.size = array1.length;
		for(int i = 0; i < heap.size; i++){
			heap.array[i] = new DHeap_Item("", array1[i]);
			heap.array[i].setPos(i);
		}
		numOfComparisons += heap.arrayToHeap(heap.array);
		for(int i = 0; i < heap.array.length; i++){
			array1[i] = heap.Get_Min().getKey();
			numOfComparisons += heap.Delete_Min();
		}
		return numOfComparisons;
	}
}
