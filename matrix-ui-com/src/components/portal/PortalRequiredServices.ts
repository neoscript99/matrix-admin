import { DomainService } from 'matrix-ui-service';
import { PortletDataSourceService } from '../../services/PortletDataSourceService';

export interface PortalRequiredServices {
  portletColRelService: DomainService;
  portletDataSourceService: PortletDataSourceService;
  portalRowRelService: DomainService;
  portletTabRelService: DomainService;
  portletCalendarService: DomainService;
  portletLinkService: DomainService;
  portletListViewService: DomainService;
  portletTableService: DomainService;
}
