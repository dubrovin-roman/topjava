const mealAjaxUrl = "profile/meals/";
let startDate = $("#startDate");
let endDate = $("#endDate");
let startTime = $("#startTime");
let endTime = $("#endTime");

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === 'display') {
                            return formatDate(date);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderEditBtn
                },
                {
                    "defaultContent": "",
                    "orderable": false,
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
    $.datetimepicker.setLocale(navigator.language.substring(0, 2));
    startDate.datetimepicker({
        timepicker: false,
        format: "Y-m-d",
        onShow: function (param) {
            this.setOptions({
                maxDate: endDate.val() ? endDate.val() : false
            })
        }
    });
    endDate.datetimepicker({
        timepicker: false,
        format: "Y-m-d",
        onShow: function (param) {
            this.setOptions({
                minDate: startDate.val() ? startDate.val() : false
            })
        }
    });
    startTime.datetimepicker({
        datepicker: false,
        format: "H:i",
        onShow: function (param) {
            this.setOptions({
                maxTime: endTime.val() ? endTime.val() : false
            })
        }
    });
    endTime.datetimepicker({
        datepicker: false,
        format: "H:i",
        onShow: function (param) {
            this.setOptions({
                minTime: startTime.val() ? startTime.val() : false
            })
        }
    });
    $("#dateTime").datetimepicker({
        format: "Y-m-d H:i",
    });
});