package com.a.platform.base.service.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 存储方案控制器
 *
 * @author zh
 * @version v7.0.0
 * @since v7.0.0
 * 2018-03-22 10:54:47
 */
@RestController
@RequestMapping("/uploaders")
@Api(description = "上传图片api")
public class FileBaseController {

//    @Autowired
//    private FileManager fileManager;
//
//
//    @ApiOperation(value = "文件上传", response = FileVO.class)
//    @ApiImplicitParam(name = "scene", value = "业务场景", allowableValues = "goods,shop,member,other", required = true, dataType = "String", paramType = "query")
//    @PostMapping
//    public FileVO list(MultipartFile file, String scene) throws IOException {
//        if (file != null && file.getOriginalFilename() != null) {
//            //文件类型
//            String contentType= file.getContentType();
//            //获取文件名称
//            String ext = contentType.substring(contentType.lastIndexOf("/") + 1, contentType.length());
//            if (!FileUtil.isAllowUpImg(ext)) {
//                throw new ServiceException(SystemErrorCode.E901.code(), "不允许上传的文件格式，请上传gif,jpg,bmp格式文件。");
//            }
//            FileDTO input = new FileDTO();
//            input.setName(file.getOriginalFilename());
//            input.setStream(file.getInputStream());
//            input.setExt(ext);
//            return this.fileManager.upload(input, scene);
//        } else {
//            throw new ResourceNotFoundException("没有文件");
//        }
//    }
//
//    @ApiOperation(value = "文件删除")
//    @ApiImplicitParam(name = "file_path", value = "文件路径", required = true, dataType = "String", paramType = "query")
//    @DeleteMapping
//    public String delete(@ApiIgnore String filePath) {
//        this.fileManager.deleteFile(filePath);
//        return null;
//    }


}
