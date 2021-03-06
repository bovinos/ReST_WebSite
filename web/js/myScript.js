
Element.prototype.addClass = function(c) {
    if (this.hasAttribute("class")) {
        if (this.getAttribute("class").indexOf(c) !== -1) {
            return;
        }
        this.setAttribute("class", this.getAttribute("class") + " " + c + " ");
    } else {
        this.setAttribute("class", c + " ");
    }
};
Element.prototype.removeClass = function(c) {
    if (this.hasAttribute("class") && this.getAttribute("class").indexOf(c) !== -1) {
        var classArray = this.getAttribute("class").split(" ");
        classArray.splice(classArray.indexOf(c), 1);
        var newClass = classArray.join(" ");
        this.setAttribute("class", newClass);
    }
};
Element.prototype.toggleClass = function(c) {
    if (this.hasAttribute("class") && this.getAttribute("class").indexOf(c) !== -1) {
        this.removeClass(c);
    } else {
        this.addClass(c);
    }
};
// singleton
var MyDatePicker = (function() {

    var datePickerInput;
    var myDatePicker;
    var year;
    var month;
    var days;
    var previousMonth;
    var nextMonth;
    var activeDay;
    function createMyDatePickerMonthAndDay() {
        var tr = document.createElement("tr");
        // primo th
        previousMonth = document.createElement("th");
        previousMonth.setAttribute("id", "previousMonth");
        var span = document.createElement("span");
        span.addClass("arrow_carrot-2left");
        previousMonth.appendChild(span);
        tr.appendChild(previousMonth);
        // secondo th
        var th = document.createElement("th");
        th.setAttribute("id", "monthAndYear");
        th.setAttribute("colspan", "5");
        // primo span
        month = document.createElement("span");
        month.setAttribute("id", "month");
        th.appendChild(month);
        // secondo span
        year = document.createElement("span");
        year.setAttribute("id", "year");
        th.appendChild(year);
        tr.appendChild(th);
        // terzo th
        nextMonth = document.createElement("th");
        nextMonth.setAttribute("id", "nextMonth");
        span = document.createElement("span");
        span.addClass("arrow_carrot-2right");
        nextMonth.appendChild(span);
        tr.appendChild(nextMonth);
        return tr;
    }

    function createMyDatePickerDaysOfWeek() {
        var tr = document.createElement("tr");
        var days = ["Lu", "Ma", "Me", "Gi", "Ve", "Sa", "Do"];
        var th;
        for (var i in days) {
            th = document.createElement("th");
            th.addClass("dayOfWeek");
            th.appendChild(document.createTextNode(days[i]));
            tr.appendChild(th);
        }

        return tr;
    }

    function createMyDatePickerHead() {
        var thead = document.createElement("thead");
        thead.appendChild(createMyDatePickerMonthAndDay());
        thead.appendChild(createMyDatePickerDaysOfWeek());
        return thead;
    }

    function createMyDatePickerBody() {
        var tbody = document.createElement("tbody");
        var tr;
        days = [];
        for (var i = 0; i < 42; i++) {
            if ((i % 7) === 0) {
                if (i !== 0) {
                    tbody.appendChild(tr);
                }
                tr = document.createElement("tr");
            }
            days[i] = document.createElement("td");
            days[i].addClass("day");
            tr.appendChild(days[i]);
        }
        tbody.appendChild(tr);
        return tbody;
    }

    function createMyDatePickerStructure() {
        myDatePicker = document.createElement("div");
        myDatePicker.setAttribute("id", "myDatePickerContainer");
        var div = document.createElement("div");
        div.setAttribute("id", "myDatePicker");
        var table = document.createElement("table");
        table.appendChild(createMyDatePickerHead());
        table.appendChild(createMyDatePickerBody());
        div.appendChild(table);
        myDatePicker.appendChild(div);
        return myDatePicker;
    }

    function initMyDatePickerValue() {
        setMyDatePickerValue(activeDay.getMonth(), activeDay.getFullYear());
    }

    function setMyDatePickerValue(newMonth, newYear) {
        month.val = newMonth;
        month.innerHTML = getStringMonth(month.val);
        year.val = newYear;
        year.innerHTML = year.val;
        var firstOfMonth = new Date(year.val, month.val, 1);
        for (var i = 0; i < days.length; i++) {
            days[i].setAttribute("class", "day");
            days[i].val = new Date(firstOfMonth.getTime() - (((firstOfMonth.getDay() === 0 ? 6 : firstOfMonth.getDay() - 1) - i) * 24 * 60 * 60 * 1000));
            days[i].innerHTML = days[i].val.getDate();
            if (days[i].val.getMonth() < month.val) {
                days[i].addClass("oldMonthDay");
            } else if (days[i].val.getMonth() > month.val) {
                days[i].addClass("newMonthDay");
            }
            if (activeDay.getDate() === days[i].val.getDate() && activeDay.getMonth() === days[i].val.getMonth() && activeDay.getFullYear() === days[i].val.getFullYear()) {
                days[i].addClass("activeDay");
            }
        }
    }

    function getStringMonth(month) {
        switch (month) {
            case 0:
                return "Gennaio";
                break;
            case 1:
                return "Febbraio";
                break;
            case 2:
                return "Marzo";
                break;
            case 3:
                return "Aprile";
                break;
            case 4:
                return "Maggio";
                break;
            case 5:
                return "Giugno";
                break;
            case 6:
                return "Luglio";
                break;
            case 7:
                return "Agosto";
                break;
            case 8:
                return "Settembre";
                break;
            case 9:
                return "Ottobre";
                break;
            case 10:
                return "Novembre";
                break;
            case 11:
                return "Dicembre";
                break;
        }
    }

    function setEventListenerToMyDatePicker() {

        document.addEventListener("click", function(e) {
            if (e.target !== myDatePicker.firstChild && datePickerInput.parentNode.lastChild === myDatePicker) {
                datePickerInput.parentNode.removeChild(myDatePicker);
                e.stopPropagation();
            } else if (e.target === datePickerInput && datePickerInput.parentNode.lastChild !== myDatePicker) {
                // reimpostiamo il giorno attivo in base all'input
                if (datePickerInput.value && new RegExp("(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})").test(datePickerInput.value)) {
                    var dateArray = datePickerInput.value.split("/");
                    activeDay = new Date(dateArray[1] + "/" + dateArray[0] + "/" + dateArray[2]);
                } else {
                    activeDay = new Date();
                }
                initMyDatePickerValue();
                datePickerInput.parentNode.appendChild(myDatePicker);
                e.stopPropagation();
            }
        });
        previousMonth.addEventListener("click", function(e) {
            month.val == 0 ? setMyDatePickerValue(11, (year.val - 1)) : setMyDatePickerValue((month.val - 1), year.val);
            e.stopPropagation();
        });
        previousMonth.onselectstart = function() {
            return false;
        }; // explorer
        previousMonth.onmousedown = function() {
            return false;
        }; // mozilla & chrome
        nextMonth.addEventListener("click", function(e) {
            month.val == 11 ? setMyDatePickerValue(0, (year.val + 1)) : setMyDatePickerValue((month.val + 1), year.val);
            e.stopPropagation();
        });
        nextMonth.onselectstart = function() {
            return false;
        }; // explorer
        nextMonth.onmousedown = function() {
            return false;
        }; // mozilla & chrome
        for (var i = 0; i < days.length; i++) {
            days[i].addEventListener("click", function(e) {
                var day = e.target.val.getDate() < 10 ? "0" + e.target.val.getDate() : e.target.val.getDate();
                var month = e.target.val.getMonth() < 9 ? "0" + (e.target.val.getMonth() + 1) : (e.target.val.getMonth() + 1);
                datePickerInput.value = day + "/" + month + "/" + e.target.val.getFullYear();
                for (var i = 0; i < days.length; i++) {
                    if (days[i].val.getDate() === activeDay.getDate() && days[i].val.getMonth() === activeDay.getMonth() && days[i].val.getFullYear() === activeDay.getFullYear()) {
                        days[i].removeClass("activeDay");
                    }
                }
                activeDay = e.target.val;
                e.target.addClass("activeDay");
                e.stopPropagation();
            });
            days[i].onselectstart = function() {
                return false;
            }; // explorer
            days[i].onmousedown = function() {
                return false;
            }; // mozilla & chrome
        }
    }

    function createMyDatePicker() {
        createMyDatePickerStructure();
        setEventListenerToMyDatePicker();
        return myDatePicker;
    }

    return function(DOMelement) {
        datePickerInput = DOMelement;

        if (!myDatePicker) {
            createMyDatePicker();
        }

        return myDatePicker;
    };
})();

// singleton
var MyTimePicker = (function() {

    var timePickerInput;
    var myTimePicker;
    var hoursUp;
    var hoursValue;
    var hoursDown;
    var minutesUp;
    var minutesValue;
    var minutesDown;

    function createMyTimePickerHours() {
        var div = document.createElement("div");
        div.setAttribute("id", "hours");
        hoursUp = document.createElement("span");
        hoursUp.addClass("arrow");
        hoursUp.addClass("arrow_carrot-up");
        div.appendChild(hoursUp);
        hoursValue = document.createElement("p");
        div.appendChild(hoursValue);
        hoursDown = document.createElement("span");
        hoursDown.addClass("arrow");
        hoursDown.addClass("arrow_carrot-down");
        div.appendChild(hoursDown);
        return div;
    }

    function createMyTimePickerMinutes() {
        var div = document.createElement("div");
        div.setAttribute("id", "minutes");
        minutesUp = document.createElement("span");
        minutesUp.addClass("arrow");
        minutesUp.addClass("arrow_carrot-up");
        div.appendChild(minutesUp);
        minutesValue = document.createElement("p");
        div.appendChild(minutesValue);
        minutesDown = document.createElement("span");
        minutesDown.addClass("arrow");
        minutesDown.addClass("arrow_carrot-down");
        div.appendChild(minutesDown);
        return div;
    }

    function createMyTimePickerStructure() {
        myTimePicker = document.createElement("div");
        myTimePicker.setAttribute("id", "myTimePickerContainer");
        var div = document.createElement("div");
        div.setAttribute("id", "myTimePicker");
        div.appendChild(createMyTimePickerHours());
        div.appendChild(createMyTimePickerMinutes());
        myTimePicker.appendChild(div);
        return myTimePicker;
    }

    function initMyTimePickerValue() {
        hoursValue.val = 0;
        hoursValue.innerHTML = "0" + hoursValue.val;
        minutesValue.val = 0;
        minutesValue.innerHTML = "0" + minutesValue.val;
    }

    function setMyTimePickerValue(hours, minutes) {
        hoursValue.val = hours;
        hoursValue.val <= 9 ? hoursValue.innerHTML = "0" + hoursValue.val : hoursValue.innerHTML = hoursValue.val;
        minutesValue.val = minutes;
        minutesValue.val <= 9 ? minutesValue.innerHTML = "0" + minutesValue.val : minutesValue.innerHTML = minutesValue.val;
    }

    function setEventListenerToMyTimePicker() {

        document.addEventListener("click", function(e) {
            if (e.target !== myTimePicker.firstChild && timePickerInput.parentNode.lastChild === myTimePicker) {
                timePickerInput.parentNode.removeChild(myTimePicker);
                e.stopPropagation();
            } else if (e.target === timePickerInput && timePickerInput.parentNode.lastChild !== myTimePicker) {
                // reimpostiamo il tempo in base all'input
                if (timePickerInput.value && new RegExp("([01][0-9]|2[0-3]):([0-5][0-9])").test(timePickerInput.value)) {
                    var timeArray = timePickerInput.value.split(":");
                    setMyTimePickerValue(parseInt(timeArray[0]), parseInt(timeArray[1]));
                } else {
                    initMyTimePickerValue();
                }
                timePickerInput.parentNode.appendChild(myTimePicker);
                e.stopPropagation();
            }
        });
        hoursUp.addEventListener("click", function(e) {
            hoursValue.val >= 23 ? hoursValue.val = 0 : hoursValue.val++;
            hoursValue.val <= 9 ? hoursValue.innerHTML = "0" + hoursValue.val : hoursValue.innerHTML = hoursValue.val;
            timePickerInput.value = hoursValue.innerHTML + ":" + minutesValue.innerHTML;
            e.stopPropagation();
        });
        hoursUp.onselectstart = function() {
            return false;
        }; // explorer
        hoursUp.onmousedown = function() {
            return false;
        }; // mozilla & chrome
        hoursDown.addEventListener("click", function(e) {
            hoursValue.val <= 0 ? hoursValue.val = 23 : hoursValue.val--;
            hoursValue.val <= 9 ? hoursValue.innerHTML = "0" + hoursValue.val : hoursValue.innerHTML = hoursValue.val;
            timePickerInput.value = hoursValue.innerHTML + ":" + minutesValue.innerHTML;
            e.stopPropagation();
        });
        hoursDown.onselectstart = function() {
            return false;
        }; // explorer
        hoursDown.onmousedown = function() {
            return false;
        }; // mozilla & chrome
        minutesUp.addEventListener("click", function(e) {
            minutesValue.val >= 59 ? minutesValue.val = 0 : minutesValue.val++;
            minutesValue.val <= 9 ? minutesValue.innerHTML = "0" + minutesValue.val : minutesValue.innerHTML = minutesValue.val;
            timePickerInput.value = hoursValue.innerHTML + ":" + minutesValue.innerHTML;
            e.stopPropagation();
        });
        minutesUp.onselectstart = function() {
            return false;
        }; // explorer
        minutesUp.onmousedown = function() {
            return false;
        }; // mozilla & chrome
        minutesDown.addEventListener("click", function(e) {
            minutesValue.val <= 0 ? minutesValue.val = 59 : minutesValue.val--;
            minutesValue.val <= 9 ? minutesValue.innerHTML = "0" + minutesValue.val : minutesValue.innerHTML = minutesValue.val;
            timePickerInput.value = hoursValue.innerHTML + ":" + minutesValue.innerHTML;
            e.stopPropagation();
        });
        minutesDown.onselectstart = function() {
            return false;
        }; // explorer
        minutesDown.onmousedown = function() {
            return false;
        }; // mozilla & chrome
    }

    function createMyTimePicker() {

        createMyTimePickerStructure();
        setEventListenerToMyTimePicker();
        return myTimePicker;
    }

    return function(DOMelement) {
        timePickerInput = DOMelement;

        if (!myTimePicker) {
            createMyTimePicker();
        }

        return myTimePicker;
    };
})();

window.onload = function() {

    // tolgiamo la possibilità di poter selezionare il testo delle label
    // in modo da far funzionare al meglio le checkbox
    var elements = document.querySelectorAll("label");
    for (var i = 0; i < elements.length; i++) {
        elements[i].onselectstart = function() {
            return false;
        }; // explorer
        elements[i].onmousedown = function() {
            return false;
        }; // mozilla & chrome
    }

    // DATE PICKER
    var datePickerInputs = document.querySelectorAll(".myDatePickerInput");
    for (var i = 0; i < datePickerInputs.length; i++) {
        datePickerInputs[i].addEventListener("click", function(e) {
            MyDatePicker(e.target);
        });
    }
    // TIME PICKER
    var timePickerInputs = document.querySelectorAll(".myTimePickerInput");
    for (var i = 0; i < timePickerInputs.length; i++) {
        timePickerInputs[i].addEventListener("click", function(e) {
            MyTimePicker(e.target);
        });
    }
};
