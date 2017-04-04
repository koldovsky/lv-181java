'use strict';
let abonnementTable;
$(function () {
    abonnementTable = buildDataTable('.abonnement-datatable', 'adm-pag-abonnements', columns, abonnementsFunctions);
});

let columns = [
    {
        'data': 'name',
        'render': function (data, type, full) {
            return "<div class='name'>" + full.name + "</div>";
        }
    },
    {
        'data': 'price',
        'render': function (data, type, full) {
            return "<div class='price'>" + full.price + "</div>";
        }
    },
    {
        'data': 'hour',
        'render': function (data, type, full) {
            return "<div class='hour'>" + full.hour + "</div>";
        }
    },
    {
        'data': null,
        'render': function () {
            return "<span><a tabindex='-1'>" +
                "<button class='btn btn-raised btn-info btn-edit' " +
                "data-toggle='modal' data-target='#updateAbonnement'>" +
                "<i class='glyphicon glyphicon-pencil'></i></button></a></span>";
        },
        'orderable': false
    },
    {
        'data': null,
        'render': function () {
            return "<span class='assign'><button class='btn btn-success btn-responsive pull-center'>" +
                "<span class='glyphicon glyphicon-user'>" +
                "</button></span>";
        },
        'orderable': false
    },
    {
        'data': null,
        'render': function () {
            return "<span><label class='switch'>" +
                "<input type='checkbox' checked class='activate'>" +
                "<div class='slider round'></div>" +
                "</label></span>";
        },
        'orderable': false
    }
];

const abonnementsFunctions = function () {

    $('#createAbonnementForm').submit(function (event) {
        let path = 'adm-abonnement';
        event.preventDefault();
        let dataSender = {
            id: $(".createId").val(),
            name: $(".createName").val(),
            price: $(".createPrice").val(),
            hour: $(".createHour").val()
        };
        $.ajax({
            url: path,
            type: 'POST',
            contentType: 'application/json',
            datatype: 'json',
            data: JSON.stringify(dataSender),
            success: function () {
                abonnementTable.ajax.reload(null, false);
            },
            error: function (data) {
                console.log(data);
            }
        });
    });

    $('#updateAbonnementForm').submit(function (event) {
        let path = 'adm-abonnement';
        event.preventDefault();
        let dataSender = {
            id: $(".updateId").val(),
            name: $(".updateName").val(),
            price: $(".updatePrice").val(),
            hour: $(".updateHour").val()
        };
        $.ajax({
            url: path,
            type: 'PUT',
            contentType: 'application/json',
            datatype: 'json',
            data: JSON.stringify(dataSender),
            success: function () {
                abonnementTable.ajax.reload(null, false);
            },
            error: function () {
            }
        });
    });

    // initing update Modal
    $(document.body).on('click', '.btn-edit', function () {
        let idAbonnement = getId(this);
        let path = 'adm-abonnement/' + idAbonnement;
        $.ajax({
            url: path,
            success: function (data) {
                $(".updateId").val(data.id);
                $(".updateName").val(data.name);
                $(".updatePrice").val(data.price);
                $(".updateHour").val(data.hour);
            },
            error: function () {
            }
        });
    });

    // update Abonnement active state
    $(document.body).on('change', '.activate', function() {
        let idAbonnement = getId(this);
        let path = 'adm-active-abonnement';
        let dataSender = {
            id: idAbonnement,
            active: this.checked
        };
        $.ajax({
            url: path,
            type: 'PUT',
            contentType: 'application/json',
            datatype: 'json',
            data: JSON.stringify(dataSender),
            success: function (data) {
            }
        });
    });
};