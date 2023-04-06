package controller;

import dto.ChannelDTO;
import service.ChannelService;
import service.ChannelServiceImpl;
import view.ChannelSuccessView;
import view.FailView;

import java.util.List;

public class ChannelController {
    private ChannelController() {

    }

    private static ChannelController instance = new ChannelController();

    public static ChannelController getInstance() {
        return instance;
    }
    private static ChannelService channelService = ChannelServiceImpl.getInstance();

    /**
     * 현재 상태에서 볼 수 있는 채널 리스트 출력
     */
    public static List<ChannelDTO> channelSelect() {
        List<ChannelDTO> ret = null;
        try {
            ret = channelService.visibleChannelSelect();
            ChannelSuccessView.selectPrint(channelService.visibleChannelSelect());
        } catch (RuntimeException e) {
            FailView.errorMessage(e.getMessage());
        }
        return ret;
    }

    /**
     * 채널 추가(관리자)
     * @param channelDTO
     */
    public static void channelInsert(ChannelDTO channelDTO) {
        try {
            channelService.addChannel(channelDTO);
            ChannelSuccessView.messagePrint("채널이 등록되었습니다");
        } catch (RuntimeException e) {
            FailView.errorMessage(e.getMessage());
        }
    }

    /**
     * 채널 업데이트(관리자)
     * @param channelDTO
     */
    public static void channelUpdate(ChannelDTO channelDTO) {
        try {
            channelService.updateChannel(channelDTO);
            ChannelSuccessView.messagePrint("채널이 업데이트 되었습니다");
        } catch (RuntimeException e) {
            FailView.errorMessage(e.getMessage());
        }
    }

    public static void channelDelete(long channel_id) {
        try {
            channelService.deleteChannel(channel_id);
            ChannelSuccessView.messagePrint("채널이 삭제되었습니다");
        } catch (RuntimeException e) {
            FailView.errorMessage(e.getMessage());
        }
    }
}
