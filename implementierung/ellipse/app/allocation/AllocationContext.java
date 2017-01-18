package allocation;
/**
 * Allocation context der festlegt welcher Allocator verwendet wird
 * @author emil
 *
 */
public class AllocationContext {
	private AbstractAllocator allocator =null;
	
	public void setAllocator(AbstractAllocator allocator){
		this.allocator = allocator;
	}
	public void calculate(Configuration config){
		if(allocator != null) {
			allocator.calculate(config);
		}
	}
	/**
	 * bricht die berechnung ab
	 */
	public void cancelAllocation(){
		if(allocator != null){
			allocator.cancel();
		}
	}

}
