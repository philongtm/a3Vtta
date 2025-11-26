function tempListFunc() {
    form = document.forms[0];
        form.tempFileInfo.value = "";
    if (form.item("index") != null) {
        tempValue = "";
        indexObj = form.item("index");
        etsuranKengenObj = form.item("etsuranKengen");
        delFlgObj = form.item("delFlg");
        if (form.item("index").length) {
            for (i = 0; i < form.item("index").length; i++) {
                indexObj = form.item("index",i);
                etsuranKengenObj = form.item("etsuranKengen",i);
                delFlgObj = form.item("delFlg",i);
                tempValue = tempValue + indexObj.value + "," + etsuranKengenObj.value + "," + delFlgObj.checked + ":";
            }
        } else {
            tempValue = indexObj.value + "," + etsuranKengenObj.value + "," + delFlgObj.checked;
        }
        form.tempFileInfo.value = tempValue;
	}
}