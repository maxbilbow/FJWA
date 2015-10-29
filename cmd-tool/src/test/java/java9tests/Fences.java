package java9tests;
/**
 * A set of methods providing fine-grained control of memory ordering.
 *
 * <p>The Java Language Specification permits operations to be
 * executed in orders different than are apparent in program source
 * code, subject to constraints mainly arising from the use of locks
 * and volatile fields. The methods of this class can also be used to
 * impose constraints. Their specifications are phrased in terms of
 * the lack of "reorderings" -- observable ordering effects that might
 * otherwise occur if the fence were not present.
 *
 * @apiNote More precise phrasing of these specifications may
 * accompany future updates of the Java Language Specification.
 */
public class Fences {

    /**
     * Ensures that loads and stores before the fence will not be
     * reordered with loads and stores after the fence.
     *
     * @apiNote Ignoring the many semantic differences from C and
     * C++, this method has memory ordering effects compatible with
     * atomic_thread_fence(memory_order_seq_cst)
     */
    public static void fullFence() {}

    /**
     * Ensures that loads before the fence will not be reordered with
     * loads and stores after the fence.
     *
     * @apiNote Ignoring the many semantic differences from C and
     * C++, this method has memory ordering effects compatible with
     * atomic_thread_fence(memory_order_acquire)
     */
    public static void acquireFence() {}

    /**
     * Ensures that loads and stores before the fence will not be
     * reordered with stores after the fence.
     *
     * @apiNote Ignoring the many semantic differences from C and
     * C++, this method has memory ordering effects compatible with
     * atomic_thread_fence(memory_order_release)
     */
    public static void releaseFence() {}

    /**
     * Ensures that loads before the fence will not be reordered with
     * loads after the fence.
     */
    public static void loadLoadFence() {}

    /**
     * Ensures that stores before the fence will not be reordered with
     * stores after the fence.
     */
    public static void storeStoreFence() {}

}