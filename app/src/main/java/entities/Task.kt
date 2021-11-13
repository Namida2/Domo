package entities

typealias TaskWithEmployee = Task<Employee, Unit>
interface Task<ASuccess, RSuccess> {
    fun onSuccess(arg: ASuccess): RSuccess
    fun onError(arg: ErrorMessage? = null)
}