<#import "/spring.ftl" as spring />
<#import "paginator.ftl" as paginator>

<!DOCTYPE html>
<html ng-app="crud">
<head>
    <title>  ${model.displayName} - Admin </title>

    <script type="text/javascript" src="<@spring.url '/simplecrud/js/jquery/jquery-1.8.2.min.js'/>"></script>
    <script type="text/javascript" src="<@spring.url '/simplecrud/js/bootstrap/bootstrap.min.js'/>"></script>

    <link rel="stylesheet" type="text/css" href="<@spring.url '/simplecrud/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/simplecrud/css/motech-paginator-pagination.css'/>"/>
    <link rel="stylesheet" type="text/css" href="<@spring.url '/simplecrud/css/crud.css'/>"/>
</head>

<body class="main">
<h2>${model.displayName} - Listing</h2>

<div class="row-fluid">
    <@paginator.filter id = "filter"  pagination_id = "listing">
        <div class="well" id="search-section">
            <div id="search-pane">
                <fieldset class="filters">
                <div class="row-fluid sel-result">
                    <#list model.filterFields as filterField>

                        <div class="control-group span2">
                            <label class="control-label">${filterField}</label>

                            <div class="controls">
                                <input type="text" name="${filterField}" id="${filterField}"
                                       value="{{searchCriteria.${filterField}}}"/>
                            </div>
                        </div>

                    </#list>
                </div>
                </fieldset>
                <div class="control-group buttons-group row-fluid">
                    <div class="controls pull-right">
                        <button id="clearFilter" ng-click="clearFormFieldsAndSearchCriteria()" type="reset"
                                class="btn "><i class="icon-remove"></i> Clear All
                        </button>
                        <button type="submit" id="searchButton" class="btn btn-primary">
                            Search <i class="icon-search icon-white"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </@paginator.filter>
</div>
<div class="row-fluid">
    <div class="results">
        <div id="${entity}s">
            <@paginator.paginate id = "listing" entity="${entity}" filterSectionId="filter" contextRoot="${contextRoot}" rowsPerPage="20">
                <table id="${entity}List" class="table table-striped table-bordered table-condensed"
                       redirectOnRowClick="true">
                    <thead>
                    <tr>
                        <#list model.displayFields as displayField>
                        <th>${displayField}</th>
                        </#list>
                        <#if model.allowUpdate()>
                        <th>Edit</th>
                        </#if>
                        <#if model.allowDelete()>
                        <th>Delete</th>
                        </#if>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in data.results">
                        <#list model.displayFields as displayField>
                            <td id="${entity}_{{item.${displayField}}}">{{item.${displayField}}}</td>
                        </#list>
                        <#if model.allowUpdate() == true>
                        <td><a href="#" class = "editEntity" data-toggle="modal" data-target="#createOrEditEntityModal" onclick="editEntity(this)" entityId = "{{item.${model.idFieldName}}}">Edit</a></td>
                        </#if>
                        <#if model.allowDelete()>
                        <td><a href="#" class = "deleteEntity" onclick="deleteEntity(this)" entityId = "{{item.${model.idFieldName}}}">Delete</a></td>
                        </#if>
                    </tr>
                    <tr type="no-results" class="hide">
                        <td class="warning text-center" colspan="17"></td>
                    </tr>
                    </tbody>
                </table>
            </@paginator.paginate>
        </div>
    </div>
    <#if model.allowCreate()>
    <a href= "#" data-toggle="modal" data-target="#createOrEditEntityModal" onclick="newEntity(this)">Add Entity</a>
    </#if>
</div>

<div id="createOrEditEntityModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="modalLabel">Add or Edit Entity</h3>
    </div>
    <div class="modal-body">
        <form id = "jsonForm"></form>
    </div>
</div>

</div>

</body>
<script type="text/javascript" src="<@spring.url '/simplecrud/js/paginator/angular.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/simplecrud/js/paginator/pagination.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/simplecrud/js/paginator/filter.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/simplecrud/js/jsonform/underscore.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/simplecrud/js/crud/crudHelper.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/simplecrud/js/jsonform/jsv.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/simplecrud/js/jsonform/jsonform.js'/>"></script>

<script type="text/javascript">

    function reloadPage(){
        angular.element($('#listing .paginator')).controller().loadPage();
    }

    function deleteEntity(elem){
        var answer = confirm('Are you sure?');
        if (answer) {
        $.ajax({
            type:"GET",
            url:"<@spring.url '/crud/${entity}/delete/'/>" + $(elem).attr('entityId'),
            success:function (data) {
                reloadPage();
            }
        });
        }
    }

    function editEntity(elem){
        $('#jsonForm').contents().remove();

        $.getJSON("<@spring.url '/crud/${entity}/get/'/>" +  $(elem).attr('entityId'), function (json) {
            $('#jsonForm').jsonForm({
                "schema":jsonSchema.properties,
                "form":[
                    "*",
                    <#list model.hiddenFields as hiddenField>
                    {"key":"${hiddenField}", "type":"hidden"},
                    </#list>
                    {"type":"submit", "title":"Submit"}
                ],
                "onSubmitValid": submitValues,
                "value": json
            });
        });
    }

    function newEntity(elem){
        $('#jsonForm').contents().remove();
        $('#jsonForm').jsonForm({
            "schema":jsonSchema.properties,
            "form":[
                "*",
            <#list model.hiddenFields as hiddenField>
                {"key":"${hiddenField}", "type":"hidden"},
            </#list>
                {"type":"submit", "title":"Submit"}
            ],
            "onSubmitValid": submitValues
        });
    }

    function submitValues(values){
        $.ajax("<@spring.url '/crud/${entity}/save/'/>", {
            data:JSON.stringify(trimmed(values)),
            contentType:'application/json',
            type:'POST'
        }).done(function () {
                    reloadPage();
                    $('#createOrEditEntityModal').modal('hide');
                });
    }

    var jsonSchema;
    $(document).ready(function(){
        $.getJSON("<@spring.url '/crud/${entity}/schema/'/>", function (json) {
            jsonSchema = json;
            <#list model.defaultValues?keys as key>
                jsonSchema.properties.${key}.default = "${model.defaultValues[key]}";
            </#list>
            <#--jsonSchema.properties.type.default = "${entity}";-->
        });
    });
</script>
</html>
