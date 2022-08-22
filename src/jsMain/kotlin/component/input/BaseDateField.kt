package component.input

import domain.use
import kotlinx.datetime.LocalDate
import mui.lab.DatePicker
import mui.lab.LocalizationProvider
import mui.material.TextField
import npm.date.fns.localePl
import npm.mui.lab.AdapterDateFns
import react.*
import kotlin.js.Date

external interface BaseDateFieldProps : Props {
    var label: String
    var initialValue: LocalDate?
    var onChange: (LocalDate?) -> Unit
}
val BaseDateField = FC<BaseDateFieldProps> { props ->
    LocalizationProvider {
        dateAdapter = AdapterDateFns
        locale = localePl

        var jsDate by useState<Date?>(null)
        useEffect(props.initialValue) {
            jsDate = use(props.initialValue) {
                Date(year = it.year, month = it.monthNumber - 1, day = it.dayOfMonth)
            }
        }

        DatePicker {
            asDynamic().value = jsDate

            asDynamic().mask = "__.__.____"
            asDynamic().label = props.label
            asDynamic().onChange = { any: dynamic ->
                when (any) {
                    is Date -> {
                        jsDate = any as? Date
                        if (!any.getTime().isNaN()) {
                            props.onChange(
                                LocalDate(any.getFullYear(), any.getMonth() + 1, any.getDate())
                            )
                        }
                    }
                    else -> {
                        jsDate = null
                        props.onChange(null)
                    }
                }
            }
            asDynamic().renderInput = { params: Props ->
                TextField.create {
                    +params

                    fullWidth = true
                    onBlur = {
                        val currentString = params.asDynamic().inputProps.value
                            .unsafeCast<String?>().orEmpty()
                        currentString.runCatching {
                            LocalDate(
                                year = substring(6..9).toInt(),
                                monthNumber = substring(3..4).toInt(),
                                dayOfMonth = substring(0..1).toInt(),
                            )
                        }.onFailure {
                            jsDate = null
                            props.onChange(null)
                        }
                    }
                }
            }
        }
    }
}