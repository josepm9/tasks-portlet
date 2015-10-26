
<%
	final javax.portlet.PortletRequest req = (javax.portlet.PortletRequest) pageContext
			.findAttribute("javax.portlet.request");
	final javax.portlet.PortletResponse resp = (javax.portlet.PortletResponse) pageContext
			.findAttribute("javax.portlet.response");
	final String apiContext = req.getAttribute("resourceURL")
			.toString();
	final String pNamespace = resp.getNamespace();
%>

<html ng-app="tasksApp">

<link href="https://fonts.googleapis.com/icon?family=Material+Icons" 
	rel="stylesheet" />
<link href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css"
	rel="stylesheet" />
 
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.7/angular.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.7/angular-resource.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.js"></script>
<script
	src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<script>
	var appContext = "<%=apiContext%>";
	var pNamespace = "<%=pNamespace%>";

	// Create app
	var app = angular.module('tasksApp', [ 'app.services' ]);

	// Create services
	var services = angular.module('app.services', [ 'ngResource' ]);
	services.factory('TasksResource', [ '$resource', function($resource) {
		return $resource(appContext, {}, {
			post : {
				method : 'POST',
				isArray : false,
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			}
		});
	} ]);

	// Create controller
	app.controller('TasksController', [
			'$scope',
			'TasksResource',
			function($scope, TasksResource) {

				$scope._init = function() {
					$scope.task = {"pk":{}};
					$scope.tasks = {};
					$scope.paginator = {};
					$scope._cleanPaginator();
					$scope._showList();
					
					$scope.Math = window.Math;
				};
				
				$scope._adaptDatesBeforeSubmit = function(ts) {
					var t = jQuery.extend({}, ts);
					t.limit = t.limit?t.limit.getTime():null;
					t.created = t.created?t.created.getTime():null;
					t.closed = t.closed?t.closed.getTime():null;
					return t;
				};
				
				$scope._adaptDatesBeforeShowing = function(t) {
					t.limit = $scope._adaptDateBeforeShowing(t.limit);
					t.created = $scope._adaptDateBeforeShowing(t.created);
					t.closed = $scope._adaptDateBeforeShowing(t.closed);
					return t;
				};
				$scope._adaptDateBeforeShowing = function(dt) {
					if (dt) {
						return new Date(dt);
					}
					else if (dt==0) {
						return "";
					}
				};

				$scope.insertTask = function() {
					var cmd = {};
					cmd[pNamespace + "cmd"] = "Insert";
					var task = $scope._adaptDatesBeforeSubmit($scope.task);
					cmd[pNamespace + "data"] = JSON.stringify({"task":task});
					TasksResource.post($.param(cmd), $scope._gen_modification_callback, null);
					$scope._cleanTask();
				};
				
				$scope._gen_modification_callback = function(p) {
					$scope._handleErrorResponse(p);
					$scope._showList();
				};
				

				$scope.updateTask = function() {
					var cmd = {};
					cmd[pNamespace + "cmd"] = "Update";
					var task = $scope._adaptDatesBeforeSubmit($scope.task);
					cmd[pNamespace + "data"] = JSON.stringify({"task":task});
					TasksResource.post($.param(cmd), $scope._gen_modification_callback, null);
					$scope._cleanTask();
				};

				$scope.finishTask = function(id) {
					var cmd = {};
					cmd[pNamespace + "cmd"] = "Finish";
					cmd[pNamespace + "data"] = JSON.stringify({
						"taskId" : id
					});
					TasksResource.post($.param(cmd), $scope._gen_modification_callback, null);
					$scope._cleanTask();
				};

				$scope.editTask = function(id) {
					var cmd = {};
					cmd[pNamespace + "cmd"] = "Get";
					cmd[pNamespace + "data"] = JSON.stringify({
						"taskId" : id
					});
					TasksResource.post($.param(cmd), function(p) {
						$scope._showEditTask(p);
					}, null);
				};

				$scope.deleteTask = function(id) {
					var cmd = {};
					cmd[pNamespace + "cmd"] = "Delete";
					cmd[pNamespace + "data"] = JSON.stringify({
						"taskId" : id
					});
					TasksResource.post($.param(cmd), $scope._gen_modification_callback, null);
				};

				$scope.gotoPage = function(pageNum) {
					$scope.paginator.pageNum = pageNum;
					$scope._showList();
				};
				
				$scope._showList = function() {
					var cmd = {};
					cmd[pNamespace + "cmd"] = "Query";
					cmd[pNamespace + "data"] = JSON.stringify({
						"pageNum" : $scope.paginator.pageNum,
						"pageSize" : $scope.paginator.pageSize,
						"order" : $scope.paginator.order
					});
					TasksResource.post($.param(cmd), $scope._showList_callback,
							null);
				};

				$scope._showList_callback = function(ps) {
					var page = $scope._handleErrorResponse(ps);
					if (page) {
						$scope.paginator.total = page.total;
						$scope.paginator.pageNum = page.pageNum;
						$scope.paginator.pageSize = page.pageSize;
						$scope.paginator.order = page.order;
						$scope.page = page;
						if (page.entries) {
							$.each(page.entries, function( index, value ) {
								$scope._adaptDatesBeforeShowing(value);
							});
						};
						// order.property
					}
				};

				$scope._cleanTask = function() {
					$scope.task.pk = {"id":""};
					$scope.task.summary = '';
					$scope.task.limit = new Date();
					$scope.task.description = '';
					$scope.task.state = '';
					$scope.task.created = '';
					$scope.task.closed = '';
					$("#taskView").hide();
				};
				
				$scope.cancelTask = function() {
					$scope._cleanTask();
				};

				$scope._cleanPaginator = function() {
					$scope.paginator.pageNum = 0;
					$scope.paginator.pageSize = 5;
					$scope.paginator.total = -1;
					$scope.paginator.order = ["limit asc"];
				};

				$scope.isTaskValid = function() {
					return !$scope.task.pk.id || !$scope.task.summary
							|| !$scope.task.limit;
				};

				$scope.showNewTask = function() {
					$scope._cleanTask();
					$("#submitInsert").show();
					$("#submitUpdate").hide();
					$("#taskView_taskId").prop('readonly', false);
					$("#taskView").show();
				};
				$scope._showEditTask = function(t) {
					var task = $scope._handleErrorResponse(t);
					if (task) {
						$scope.task = $scope._adaptDatesBeforeShowing(task);
						$("#submitInsert").hide();
						$("#submitUpdate").show();
						$("#taskView_taskId").prop('readonly', true);
						$("#taskView").show();
					}
				};
				
				$scope._handleErrorResponse = function(resp) {
					if (resp.code=="0") {
						return resp.value;
					}
					else {
						var error = resp.value;
						$("#errorCode").text(error.code);
						$("#errorMsg").text(error.errMsg);
						$("#errorCause").empty();
						$scope._addErrorCause(error.innerError, $("#errorCause"));
						$( "#error-message" ).dialog(
							{
								modal: true, 
								buttons: {
									Ok: function() {
										$( this ).dialog( "close" );
									}
								}
							}
						);
						return 0;
					}
				};
				$scope._addErrorCause = function(errorCause, $div) {
					if (errorCause) {
						var $newdiv = $( "<div style=\"margin-left:15px\"/>" );
						var $newdivMsg = $( "<div style=\"width:100%\"/>" );
						$newdivMsg.text(errorCause.errMsg);
						$div.append($newdiv);
						$newdiv.append($newdivMsg);
						$scope._addErrorCause(errorCause.innerError, $newdiv);
					}
				};

				// Initialize
				$scope._init();

			} ]);
</script>

<body ng-controller="TasksController as app">

	<!-- VIEW -->

	<div id="taskView" style="display:none">
		<h3 class="portlet-section-header">Task</h3><br/>
		<div style="width:100%">
			<div style="width:20%;float:left;padding:0 0 0 0">
				<label for="id" class="portlet-form-field-label">Identifier:</span>
			</div>
			<div style="width:80%;float:left;padding:0 0 0 0">
				<input id="taskView_taskId" name="id" data-ng-model="task.pk.id"
					type="text" placeholder="Identifier" required class="portlet-form-input-field" />
			</div>
		</div>
		<div style="width:100%">
			<div style="width:20%;float:left;padding:0 0 0 0">
				<label for="summary" class="portlet-form-field-label">Summary:</span>
			</div>
			<div style="width:80%;float:left;padding:0 0 0 0">
				<input name="summary" data-ng-model="task.summary"
					type="text" placeholder="Summary" required class="portlet-form-input-field" />
			</div>
		</div>
		<div style="width:100%">
			<div style="width:20%;float:left;padding:0 0 0 0">
				<label for="limit" class="portlet-form-field-label">Limit:</span>
			</div>
			<div style="width:80%;float:left;padding:0 0 0 0">
				<input name="limit" data-ng-model="task.limit"
					type="date" placeholder="yyyy-MM-dd" required class="portlet-form-input-field" />
			</div>
		</div>
		<div style="width:100%">
			<div style="width:20%;float:left;padding:0 0 0 0">
				<label for="description" class="portlet-form-field-label">Description:</span>
			</div>
			<div style="width:80%;float:left;padding:0 0 0 0">
				<textarea name="description" data-ng-model="task.description"
					type="text" placeholder="Description" class="portlet-form-input-field"></textarea>
			</div>
		</div>
		<div style="width:100%">
			<div style="width:20%;float:left;padding:0 0 0 0">
				<label for="state" class="portlet-form-field-label">State:</span>
			</div>
			<div style="width:80%;float:left;padding:0 0 0 0">
				<input name="state" data-ng-model="task.state"
					type="text" placeholder="" readonly class="portlet-form-input-field" />
			</div>
		</div>
		<div style="width:100%">
			<div style="width:20%;float:left;padding:0 0 0 0">
				<label for="created" class="portlet-form-field-label">Created:</span>
			</div>
			<div style="width:80%;float:left;padding:0 0 0 0">
				<input name="created" data-ng-model="task.created"
					type="date" placeholder="" readonly class="portlet-form-input-field" />
			</div>
		</div>
		<div style="width:100%">
			<div style="width:20%;float:left;padding:0 0 0 0">
				<label for="closed" class="portlet-form-field-label">Closed:</span>
			</div>
			<div style="width:80%;float:left;padding:0 0 0 0">
				<input name="closed" data-ng-model="task.closed"
					type="date" placeholder="" readonly class="portlet-form-input-field" />
			</div>
		</div>
		<div style="width:100%">
			<button id="submitInsert" type="button" 
				data-ng-disabled="isTaskValid()" 
				data-ng-click="insertTask()">Insert Task</button>
			<button id="submitUpdate" type="button" 
				data-ng-disabled="isTaskValid()" 
				data-ng-click="updateTask()">Update Task</button>
			<button id="submitCancel" type="button" 
				data-ng-click="cancelTask()">Cancel</button>
		</div>
	</div>

	<div id="tasksView">
		<h3 class="portlet-section-header">Your tasks:
			<a ng-click="showNewTask()" href><i class="material-icons">&#xE24C;</i></a></h3><br/>
	
		<div ng-repeat="t in page.entries" style="width:100%">
			<h4 class="portlet-section-subheader">{{t.pk.id}}-{{t.summary}} 
				<a ng-click="finishTask(t.pk.id)" href><i class="material-icons" style="color:green">&#xE877;</i></a>
				<a ng-click="editTask(t.pk.id)" href><i class="material-icons">&#xE254;</i></a>
				<a ng-click="deleteTask(t.pk.id)" href><i class="material-icons" style="color:red">&#xE14C;</i></a></h4>
			<p id="description" class="portlet-section-text">{{t.description}}</p>
			<label for="limit" class="portlet-form-field-label">limit:</span>
			<span id="limit" class="portlet-section-text">{{t.limit | date: 'yyyy-MM-dd'}}</span>
			<label for="state" class="portlet-form-field-label">State:</span>
			<span id="state" class="portlet-section-text">{{t.state}}</span>
			<label for="created" class="portlet-form-field-label">Created:</span>
			<span id="created" class="portlet-section-text">{{t.created | date: 'yyyy-MM-dd'}}</span>
			<label for="closed" class="portlet-form-field-label">Closed:</span>
			<span id="closed" class="portlet-section-text">{{t.closed | date: 'yyyy-MM-dd'}}</span>
		</div>
		<!-- prev -->
		<div ng-switch on="page.pageNum>0" style="display:inline">
		    <div ng-switch-when="true" style="display:inline">
				<a ng-click="gotoPage(0)" href><i class="material-icons">&#xE5C4;</i></a>
				<a ng-click="gotoPage(page.pageNum-1)" href><i class="material-icons">&#xE5CB;</i></a>
		    </div>
		    <div ng-switch-default style="display:inline">
				<i class="material-icons" style="color:lightgray">&#xE5C4;</i>
				<i class="material-icons" style="color:lightgray">&#xE5CB;</i>
		    </div>
		</div>
		Page {{page.pageNum+1}}
		<!-- next -->
		<div ng-switch on="((page.pageNum+1)*page.pageSize) < page.total" style="display:inline">
		    <div ng-switch-when="true" style="display:inline">
				<a ng-click="gotoPage(page.pageNum+1)" href><i class="material-icons">&#xE5CC;</i></a>
				<a ng-click="gotoPage(Math.floor( (page.total-1)/page.pageSize) )" href><i class="material-icons">&#xE5C8;</i></a>
		    </div>
		    <div ng-switch-default style="display:inline">
				<i class="material-icons" style="color:lightgray">&#xE5CC;</i>
				<i class="material-icons" style="color:lightgray">&#xE5C8;</i>
		    </div>
		</div>
	</div>
	
	<div id="error-message" title="An error was detected" style="display:none">
		<div>
			<i class="material-icons" style="color: #FB0000;display:inline;">&#xE000;</i>
			[<div id="errorCode" style="display:inline"></div>]<div id="errorMsg" style="display:inline"></div>
		</div>
		<div id="errorCause"></div>
	</div>
	
	<div style="clear:both"></div>
</body>
</html>