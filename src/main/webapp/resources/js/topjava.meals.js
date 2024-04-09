const mealAjaxUrl = "profile/meals/";
let filterForm;

const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: filterMeals
};

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
    filterForm = $("#filterForm");
});

function filterMeals() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + "filter",
        data: filterForm.serialize()
    }).done(updateTableByData)
}

function clearFilter() {
    filterForm[0].reset();
    $.get(ctx.ajaxUrl, updateTableByData);
}