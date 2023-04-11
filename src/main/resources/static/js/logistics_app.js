//const main = {
//    init : function() {
//        const _this = this;
//
//        // delete 메서드
//        $('#btn-delete').on('click', function () {
//            _this.delete();
//        });
//    },
//
//
//
//    /**  'https://호스트주소/현재경로/id' 로 DELETE 메서드 요청을 보냄  **/
//    delete : function () {
//        const id = $('#id').text();
//
//        const con_check = confirm(id+"를 정말 삭제하시겠습니까?");
//
//        if(con_check == true) {
//            $.ajax({
//                type: 'DELETE',
//                url: document.location.pathname+'/'+id,
//                dataType: 'JSON',
//                contentType: 'application/json; charset=utf-8'
//            })
//            .done(function () {
//                alert("삭제되었습니다.");
//                window.location.href = document.location.pathname;
//            }).fail(function (error) {
//                alert(JSON.stringify(error));
//            });
//        } else {
//            return false;
//        }
//    }
//};
//
//main.init();