package fr.aripot007.pvpkit.util;

import java.util.List;

import fr.aripot007.pvpkit.game.Kit;


public class GUIUtil {

	/**
	 * @author Tsumifa
	 */
	public static Kit[] formatMenu(List<Kit> items){
		
		Kit[] inv = new Kit[3*9];
		
		if(items.size() < 8) {
			inv = new Kit[3*9];
		}
		
		else if(items.size() > 7 && items.size() < 15) {
			inv = new Kit[4*9];
		}
		
		else if(items.size() > 14 && items.size() < 22) {
			inv = new Kit[5*9];
		}
		else if(items.size() > 21 && items.size() < 29) {
			inv = new Kit[6*9];
		}
		
		else{
			throw new IllegalArgumentException("Too many items to display ("+items.size()+" given)");
		}
			
			
		if(items.size() == 0) {
			return inv;
		}
		
		else if(items.size() == 1) {
			inv[13] = items.get(0);
			return inv;
		}
		
		else if(items.size() == 2) {
			inv[12] = items.get(0);
			inv[14] = items.get(1); 
			return inv;
		}
		else if(items.size() == 3) {
			inv[11] = items.get(0);
			inv[13] = items.get(1);
			inv[15] = items.get(2);
			return inv;
		}
		else if(items.size() == 4) {
			inv[10] = items.get(0);
			inv[12] = items.get(1);
			inv[14] = items.get(2);
			inv[16] = items.get(3);
			return inv;
		}
		else if(items.size() == 5) {
			inv[11] = items.get(0);
			inv[12] = items.get(1);
			inv[13] = items.get(2);
			inv[14] = items.get(3);
			inv[15] = items.get(4);
			return inv;
		}
		else if(items.size() == 6) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[14] = items.get(3);
			inv[15] = items.get(4);
			inv[16] = items.get(5);
			return inv; 
		}
		else if(items.size() == 7) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			return inv;
		}
		else if(items.size() == 8) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[22] = items.get(7);
			return inv;
		}
		else if(items.size() == 9) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[21] = items.get(7);
			inv[23] = items.get(8);
			return inv;
		}
		else if(items.size() == 10) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[20] = items.get(7);
			inv[22] = items.get(8);
			inv[24] = items.get(9);
			return inv;
		}
		else if(items.size() == 11) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[21] = items.get(8);
			inv[23] = items.get(9);
			inv[25] = items.get(10);
			return inv;
		}
		else if(items.size() == 12) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[20] = items.get(7);
			inv[21] = items.get(8);
			inv[22] = items.get(9);
			inv[23] = items.get(10);
			inv[24] = items.get(11);
			return inv;
		}
		else if(items.size() == 13) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[23] = items.get(10);
			inv[24] = items.get(11);
			inv[25] = items.get(12);
			return inv;
		}
		else if(items.size() == 14) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			return inv;
		}
		else if(items.size() == 15) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[31] = items.get(14);
			return inv;
			
		}
		else if(items.size() == 16) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[30] = items.get(14);
			inv[32] = items.get(15);
			return inv;
		}
		else if(items.size() == 17) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[29] = items.get(14);
			inv[31] = items.get(15);
			inv[33] = items.get(16);
			return inv;
		}
		else if(items.size() == 18) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[30] = items.get(15);
			inv[32] = items.get(16);
			inv[34] = items.get(17);
			return inv;
		}
		else if(items.size() == 19) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[29] = items.get(14);
			inv[30] = items.get(15);
			inv[31] = items.get(16);
			inv[32] = items.get(17);
			inv[33] = items.get(18);
			return inv;
		}
		else if(items.size() == 20) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[29] = items.get(15);
			inv[30] = items.get(16);
			inv[32] = items.get(17);
			inv[33] = items.get(18);
			inv[34] = items.get(19);
			return inv;
		}
		else if(items.size() == 21) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[29] = items.get(15);
			inv[30] = items.get(16);
			inv[31] = items.get(17);
			inv[32] = items.get(18);
			inv[33] = items.get(19);
			inv[34] = items.get(20);
			return inv;
		}
		else if(items.size() == 22) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[29] = items.get(15);
			inv[30] = items.get(16);
			inv[31] = items.get(17);
			inv[32] = items.get(18);
			inv[33] = items.get(19);
			inv[34] = items.get(20);
			inv[40] = items.get(21);
			return inv;
		}
		else if(items.size() == 23) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[29] = items.get(15);
			inv[30] = items.get(16);
			inv[31] = items.get(17);
			inv[32] = items.get(18);
			inv[33] = items.get(19);
			inv[34] = items.get(20);
			inv[39] = items.get(21);
			inv[41] = items.get(22);
			return inv;
		}
		else if(items.size() == 24) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[29] = items.get(15);
			inv[30] = items.get(16);
			inv[31] = items.get(17);
			inv[32] = items.get(18);
			inv[33] = items.get(19);
			inv[34] = items.get(20);
			inv[38] = items.get(21);
			inv[40] = items.get(22);
			inv[42] = items.get(23);
			return inv;
		}
		else if(items.size() == 25) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[29] = items.get(15);
			inv[30] = items.get(16);
			inv[31] = items.get(17);
			inv[32] = items.get(18);
			inv[33] = items.get(19);
			inv[34] = items.get(20);
			inv[37] = items.get(21);
			inv[39] = items.get(22);
			inv[41] = items.get(22);
			inv[43] = items.get(23);
			return inv;
		}
		else if(items.size() == 26) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[29] = items.get(15);
			inv[30] = items.get(16);
			inv[31] = items.get(17);
			inv[32] = items.get(18);
			inv[33] = items.get(19);
			inv[34] = items.get(20);
			inv[38] = items.get(21);
			inv[39] = items.get(22);
			inv[40] = items.get(23);
			inv[41] = items.get(24);
			inv[42] = items.get(25);
			return inv;
		}
		else if(items.size() == 27) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[29] = items.get(15);
			inv[30] = items.get(16);
			inv[31] = items.get(17);
			inv[32] = items.get(18);
			inv[33] = items.get(19);
			inv[34] = items.get(20);
			inv[37] = items.get(21);
			inv[38] = items.get(22);
			inv[39] = items.get(23);
			inv[41] = items.get(24);
			inv[42] = items.get(25);
			inv[33] = items.get(26);
			return inv;
		}
		else if(items.size() == 28) {
			inv[10] = items.get(0);
			inv[11] = items.get(1);
			inv[12] = items.get(2);
			inv[13] = items.get(3);
			inv[14] = items.get(4);
			inv[15] = items.get(5);
			inv[16] = items.get(6);
			inv[19] = items.get(7);
			inv[20] = items.get(8);
			inv[21] = items.get(9);
			inv[22] = items.get(10);
			inv[23] = items.get(11);
			inv[24] = items.get(12);
			inv[25] = items.get(13);
			inv[28] = items.get(14);
			inv[29] = items.get(15);
			inv[30] = items.get(16);
			inv[31] = items.get(17);
			inv[32] = items.get(18);
			inv[33] = items.get(19);
			inv[34] = items.get(20);
			inv[37] = items.get(21);
			inv[38] = items.get(22);
			inv[39] = items.get(23);
			inv[40] = items.get(24);
			inv[41] = items.get(25);
			inv[42] = items.get(26);
			inv[43] = items.get(27);
			return inv;
		}
		return null;
		
	}
	
}