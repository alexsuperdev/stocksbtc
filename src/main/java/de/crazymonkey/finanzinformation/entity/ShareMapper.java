package de.crazymonkey.finanzinformation.entity;

public class ShareMapper {

	public static ShareValueByDate toShareInfo(SharePrice shareprice) {
		ShareValueByDate shareValueByDate = new ShareValueByDate();
		shareValueByDate.setDatum(shareprice.getPriceDate());
		shareValueByDate.setPreis(shareprice.getPrice());
		shareValueByDate.setShareName(shareprice.getShare().getSharename());
		return shareValueByDate;
	}

}
