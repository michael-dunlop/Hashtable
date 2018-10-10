/* Quadratic Probing */
	public class QuadraticProbing<AnyType>
	{
		private static final int DEFAULT_TABLE_SIZE = 13;
		private HashEntry<AnyType> [ ] array; // The array of elements
		double loadFactor = .4;
	
	public static class HashEntry<AnyType>
	{
		/* Initialize the entries here. You can write a constructor for the same */
		public AnyType  element; 
		public boolean isActive;  // For Lazy deletion
		public String toString()
		{
			if(this.element!=null)
				return (String) element;
			else
				return "NULL";
		}

	}


/* Construct the hash table */
	public QuadraticProbing( )
	{
		this( DEFAULT_TABLE_SIZE );
	}

/* Construct the hash table */

	public QuadraticProbing( int size )
	{
		/* allocate memory to hash table */
		this.array = new HashEntry [size];
		for(int i = 0; i < size; i++){
			array[i] = new HashEntry<>();
		}
	}


/* Return true if currentPos exists and is active - Lazy Deletion*/
	public boolean isActive(int position)
	{
		if(position > array.length) return false;
		else{ return  array[position].isActive; }
	}
	

/* Find an item in the hash table. */
	public boolean contains( AnyType x )
	{
		/* Should return the active status of key in hash table */
		int maxIndex = this.array.length - 1;
		int index = hash(x.toString(), this.array.length);
		int inactive = -1;
		int i = 0;
		int n;
		HashEntry e;

		while (i < maxIndex) {
			if(array[index].toString().equals(x.toString())){
				if(inactive > -1){
					e = array[inactive];
					array[inactive] = array[index];
					array[index] = e;
				}
				return true;
			} else {
				if(!array[index].isActive){
					inactive = index;
				} else if (array[index].toString().equals("NULL")){
					return false;
				}
			}
			i++;
			n = index + (i * i);
			if(n > maxIndex){
				n = n - (array.length - index);
				while(n > maxIndex){
					n = n - array.length;
				}
			}

			index = n;
		}

		return false;
	}


/* Insert into the Hash Table */
	
	public void insert( AnyType x )
	{
		/* Insert an element */
		int maxIndex = this.array.length - 1;
		int index = hash(x.toString(), this.array.length);
		int i = 0;
		int n;

		while(i < maxIndex){
			if(array[index].toString().equals("NULL")){
				array[index].element = x;
				if(!array[index].isActive){
					array[index].isActive = true;
				}

				if(((double) this.numElements()/ (double) this.array.length) >= loadFactor){
					rehash();
				}

				break;

			}

			if(array[index].toString().equals(x.toString())){
					break;
			}

			i++;
			n = index + (i * i);
			if(n > maxIndex){
				n = n - (array.length - index);
				while(n > maxIndex){
					n = n - array.length;
				}
			}

			index = n;

		}
		
	}


/* Remove from the hash table. */
	
	public void remove( AnyType x )	
	{
		/* Lazy Deletion*/
		int maxIndex = this.array.length - 1;
		int index = hash(x.toString(), this.array.length);
		int i = 0;
		int n;
		HashEntry e = new HashEntry();

		while(i < maxIndex){
			if(array[index].toString().equals(x.toString())){
				array[index] = e;
				array[index].isActive = false;
				break;

			}

			if(array[index].isActive && array[index].toString().equals("NULL")){
					break;
			}

			i++;
			n = index + (i * i);
			if(n > maxIndex){
				n = n - (array.length - index);
				while(n > maxIndex){
					n = n - array.length;
				}
			}

			index = n;
		}
   	}

   
/* Rehashing for quadratic probing hash table */
	private void rehash( )
	{
		int len = array.length;
		int n = len * 2;
		if(n == 1) n = 2;
		n = findNextPrime(n);

		QuadraticProbing<AnyType> temp = new QuadraticProbing<>(array.length);
		QuadraticProbing<AnyType> a = new QuadraticProbing<>(n);

		for(int i = 0; i < array.length; i++){
			temp.array[i] = this.array[i];
		}

		this.array = new HashEntry[n];
		for(int i = 0; i < n; i++){
			this.array[i] = new HashEntry<>();
		}


		for(int i = 0; i < len; i++ ){
			if(!temp.array[i].toString().equals("NULL"))
				this.insert(temp.array[i].element);
		}


	}
	

/* Hash Function */
	public int hash( String key, int tableSize )
	{
		/**  Make sure to type cast "AnyType"  to string 
		before calling this method - ex: if "x" is of "AnyType", 
		you should invoke this function as hash((x.toString()), tableSize) */

		int hashcode = 0;
		for(int i = 0; i < key.length(); i++){
			hashcode = (37 * hashcode + key.charAt(i)) % tableSize;
		}
		return hashcode % tableSize;
	}

	public int probe(AnyType x)
	{
		/* Return the number of probes encountered for a key */
		int maxIndex = this.array.length - 1;
		int index = hash(x.toString(), this.array.length);
		int i = 0;
		int n;

		int num_of_probes = 0;

		while(i < maxIndex) {
			if(array[index].toString().equals(x.toString())){
				break;
			} else {
				if(array[index].toString().equals("NULL") && array[index].isActive){
					break;
				}
				num_of_probes++;
			}

			i++;
			n = index + (i * i);
			if(n > maxIndex){
				n = n - (array.length - index);
				while(n > maxIndex){
					n = n - array.length;
				}
			}

			index = n;

		}

		return num_of_probes;
	}




	public int findNextPrime(int size)
	{

		while(true){
			if(size % 2 == 0) size++;
			if(isPrime(size)) break;
			size = size + 2;
		}

		return size;
	}

	public boolean isPrime(int a)
	{
		if(a <= 1) return false;
		else if (a <= 3) return true;
		else if (a % 2 == 0 || a % 3 == 0) return false;
		int i = 5;
		while(i * i <= a){
			if (a % i == 0 || a % (i + 2) == 0) return false;
			i = i + 6;
		}

		return true;
	}

	public int numElements(){
		int out = 0;
		for(int i = 0; i < array.length; i++){
			if(!array[i].toString().equals("NULL")){
				out++;
			}
		}

		return out;
	}
	
}

