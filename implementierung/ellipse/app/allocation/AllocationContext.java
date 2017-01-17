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
	public Allocation calculate(Configuration config){
		if(allocator != null) {
			return allocator.calulate(config);
		}
	}

}
