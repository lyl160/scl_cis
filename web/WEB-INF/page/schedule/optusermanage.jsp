<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/head.jsp" %>
<style>
    .M-list-b ul {
        float: left;
        height: 24px;
    }

    .M-list-b ul {
        height: 30px;
        line-height: 30px;
    }

    .M-list-b ul li, .M-list-a ul li.hover {
        width: 140px;
        margin-top: 3px;
    }

    .M-list-b ul li.hover {
        height: 20px;
        line-height: 20px;
        background: none;
        border: 2px solid #3D95D5;
        color: #3D95D5;
    }

    .M-list-b ul {
        float: left;
        height: 24px;
    }

    .M-list-b ul li {
        float: left;
        border: solid 1px #e2e1e3;
        margin-right: 10px;
        cursor: pointer;
        width: 160px;
        height: 22px;
        line-height: 22px;
        text-align: center;
    }

    ul, li {
        list-style: none;
    }
</style>
<div class="search_panel">
    <input type="hidden" id="schoolId" value="${schoolId}">
    <form action="" id="vecle_form" method="post" onsubmit="return pageSearch('#vecle_form','#grid-table1');">
        <div style="min-height:180px;">
            <div class="M-list-b">
                <ul class="M-list-ul1">
                </ul>
            </div>
        </div>
    </form>
</div>
<div class="row">
    <div class="col-xs-12">
        <table id="grid-table1"></table>
        <div id="grid-pager1"></div>
        <div style="margin-top: 10px">
				<span id="auth_btn">
					</span> <span id="paging_bar1" style="float: right"> </span>
        </div>
    </div>
    <div class="form-actions align-right  form-button-box" style="margin-top: 10px">
        <button class="btn btn-info" type="button" onclick="confirm();">
            确定
        </button>
        <button class="btn" type="button" onclick="javascript:dialog.close(this);">
            关闭
        </button>
    </div>
</div>


<!-- /.row -->
<!-- page specific plugin scripts -->
<script type="text/javascript">
    $(function () {
        var schoolId = $('#schoolId').val();
        var userIdArray = [];
        if ($('#users').val()) {
            userIdArray = $('#users').val().split(",");
        }

        $.ajax({
            type: "post",
            url: "auth/userManage/query.do",
            dataType: 'json',
            data: {
                currentPages: "1",
                pageSize: "500",
                agentId: schoolId
            },
            success: function (result) {

                var html = '';
                var data = result.rows;
                data.forEach(function (d) {
                    if (userIdArray.indexOf(d.id+"") >= 0) {
                        html += '<li class="baseBg hover"><span>' + d.id + '</span>【<i>' + d.userName + '</i>】</li>';
                    } else {
                        html += '<li class="baseBg"><span>' + d.id + '</span>【<i>' + d.userName + '</i>】</li>';
                    }
                });
                $('.M-list-ul1').append(html);

            },
            error: function (XMLHttpRequest, textStatus) {
                msg.alert("错误", "错误代码：" + XMLHttpRequest.status + ",错误描述：" + textStatus, 'error');
            }
        });
    });


    $('.M-list-ul1').on('click', 'li', function (e) {
        if ($(this).hasClass('hover')) {
            $(this).removeClass('hover');
        } else {
            $(this).addClass('hover');
        }
    });

    //确认选择值班老师
    function confirm() {

        var rows = $(".M-list-ul1 li[class='baseBg hover']");
        if (rows.length == 0) {
            msg.alert("警告", "当前没有选择数据项！", "error");
            return;
        }
        var recCode = [];
        var realname = [];
        var i = 0;
        rows.each(function () {

            var rowData = $(this).find('span').text();

            var rowData1 = $(this).find('i').text();

            recCode[i] = rowData;
            realname[i] = rowData1;
            i++;
        });
        dialog.close();
        $(window.parent.document).find("#users").val(recCode.join(","))
            .trigger('change');
        $(window.parent.document).find("#usersName")
            .val(realname.join(",")).trigger('change');
    }
</script>