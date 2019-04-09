export class ArrayExt<T> extends Array<T> {
    /**
     * Generates an array of numbers between a ragne
     * 
     * @param from Starting number of the range
     * @param to End of the range
     * @param step Steps between the count
     */
    public static range(from: number, to: number, step: number): number[] {
        return Array.from(Array(~~((to - from) / step) + 1)).map(
            (v, k) => from + k * step
        );
    }
}