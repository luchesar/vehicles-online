function AutoFillTodaysDate() {
	var dod_day = document.getElementById("dateOfDisposal_day");
	var dod_month = document.getElementById("dateOfDisposal_month");
	var dod_year = document.getElementById("dateOfDisposal_year");

  if (todaysDateOfDisposal.checked === false) {
    dod_day.value = '';
    dod_month.value = '';
    dod_year.value = '';
  } else {
    var todaysDate = new Date();
    var day = todaysDate.getUTCDate();
    var month = todaysDate.getUTCMonth() + 1; //0 based so need to increase month by 1
    var year = todaysDate. getFullYear();

    dod_day.value = day;
    dod_month.value = month;
    dod_year.value = year;
  }
}